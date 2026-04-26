package com.foxwear.orderservice.service;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.common.dto.response.ProductResponse;
import com.foxwear.common.exception.InvalidArgumentException;
import com.foxwear.common.exception.UnauthorizedException;
import com.foxwear.orderservice.client.ProductClient;
import com.foxwear.orderservice.dto.request.CartItemCreateRequest;
import com.foxwear.orderservice.dto.response.CartGetResponse;
import com.foxwear.orderservice.dto.response.CartItemCreateResponse;
import com.foxwear.orderservice.dto.response.CartItemUpdateResponse;
import com.foxwear.orderservice.entity.Cart;
import com.foxwear.orderservice.entity.CartItem;
import com.foxwear.orderservice.entity.Coupon;
import com.foxwear.orderservice.exception.*;
import com.foxwear.orderservice.mapper.CartItemMapper;
import com.foxwear.orderservice.mapper.CartMapper;
import com.foxwear.orderservice.mapper.CouponMapper;
import com.foxwear.orderservice.repository.CartItemRepository;
import com.foxwear.orderservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
    private final CouponService couponService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductClient productClient;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;
    private final CouponMapper couponMapper;

    /**
     * Creates a new cart for a specific user.
     *
     * @param userId The ID of the user for whom the cart is being created.
     */
    @Transactional
    public void createCart(Long userId) {
        log.info("Creating cart for user ID: {}", userId);

        var res = cartRepository.findByUserId(userId);

        if (res.isEmpty()) {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cartRepository.save(cart);
            log.info("Cart created successfully");
        }
    }

    /**
     * Adds a new item to the user's cart.
     *
     * @param request The DTO containing item details.
     * @param userId  The ID of the user.
     * @return CartItemCreateResponse containing the details of the added item.
     */
    @Transactional
    public CartItemCreateResponse addItemToCart(CartItemCreateRequest request, Long userId) {
        log.info("Adding item to cart for user: {}", userId);
        checkUserIdIsNotNull(userId);

        ApiResponse<ProductResponse> response = productClient.getProductWithItemId(request.getProductItemId());
        if (response == null || response.getData() == null) {
            throw new InvalidArgumentException("Product not found");
        }

        Cart cart = findCartOrThrow(userId);
        CartItem item;

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(i -> i.getProductItemId().equals(request.getProductItemId()))
                .findFirst();

        if (existingItem.isPresent()) {
            item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
        } else {
            item = cartItemMapper.toEntity(request);
            item.setCart(cart);
            cart.getItems().add(item);
        }

        item.setProductName(response.getData().getTitle());
        item.setColorName(response.getData().getColor());
        item.setImageUrl(response.getData().getImageUrl());
        item.setSizeValue(response.getData().getSize());
        item.setOriginalUnitPrice(response.getData().getOriginalPrice());
        item.setActualUnitPrice(response.getData().getDiscountPrice());
        item.setSlug(response.getData().getSlug());

        item.updateSubtotal();
        cart.updateTotalPrice();

        log.info("Item processed successfully for cart ID: {}", cart.getId());

        CartItem savedItem = cartItemRepository.save(item);
        return cartItemMapper.toCreateResponse(savedItem);
    }

    /**
     * Retrieves the cart and its items for a specific user.
     *
     * @param userId The ID of the user.
     * @return CartGetResponse containing cart details and the list of items.
     */
    @Transactional
    public CartGetResponse getCart(Long userId) {
        checkUserIdIsNotNull(userId);
        Cart cart = findCartOrThrow(userId);

        cart.getItems().forEach(item -> {
            ProductResponse currentProduct = productClient.getProductWithItemId(item.getProductItemId()).getData();

            if (item.getActualUnitPrice().compareTo(currentProduct.getDiscountPrice()) != 0) {
                item.setActualUnitPrice(currentProduct.getDiscountPrice());
                item.updateSubtotal();
                cart.updateTotalPrice();
            }
        });
        cartRepository.save(cart);

        CartGetResponse cartResponse = cartMapper.toGetResponse(cart);
        cartResponse.setItems(cart.getItems().stream()
                .map(cartItemMapper::toGetResponse)
                .toList());

        if (cart.getCoupon() != null) {
            cartResponse.setCoupon(couponMapper.toGetResponse(cart.getCoupon()));
        } else {
            cartResponse.setCoupon(null);
        }

        return cartResponse;
    }

    /**
     * Retrieves the total number of items in the user's cart.
     *
     * @param userId The ID of the user.
     * @return The total quantity of all items in the cart.
     */
    @Transactional(readOnly = true)
    public Integer getCartCount(Long userId) {
        checkUserIdIsNotNull(userId);

        return cartRepository.findByUserId(userId)
                .map(cart -> cart.getItems().stream()
                        .mapToInt(CartItem::getQuantity)
                        .sum())
                .orElse(0);
    }

    /**
     * Increases the quantity of a specific item in the cart by one.
     *
     * @param itemId The ID of the cart item.
     * @param userId The ID of the user owning the cart.
     * @return CartItemUpdateResponse with the updated item details.
     */
    @Transactional
    public CartItemUpdateResponse increaseQuantity(Long itemId, Long userId) {
        log.info("Increasing quantity for item: {} (User: {})", itemId, userId);
        checkUserIdIsNotNull(userId);

        Cart cart = findCartOrThrow(userId);
        CartItem cartItem = findCartItemOrThrow(itemId);
        checkCartIdMatch(cart, cartItem);

        cartItem.setQuantity(cartItem.getQuantity() + 1);
        cartItem.updateSubtotal();
        cart.updateTotalPrice();

        return cartItemMapper.toUpdateResponse(cartItem);
    }

    /**
     * Decreases the quantity of a specific item in the cart by one.
     * If the quantity reaches zero, the item is removed from the cart.
     *
     * @param itemId The ID of the cart item.
     * @param userId The ID of the user owning the cart.
     * @return CartItemUpdateResponse with the updated item details.
     */
    @Transactional
    public CartItemUpdateResponse decreaseQuantity(Long itemId, Long userId) {
        log.info("Decreasing quantity for item: {} (User: {})", itemId, userId);
        checkUserIdIsNotNull(userId);

        Cart cart = findCartOrThrow(userId);
        CartItem cartItem = findCartItemOrThrow(itemId);
        checkCartIdMatch(cart, cartItem);

        cartItem.setQuantity(cartItem.getQuantity() - 1);

        if (cartItem.getQuantity() <= 0) {
            cart.getItems().remove(cartItem);
            cartItemRepository.delete(cartItem);

            if (cart.getItems().isEmpty()) {
                cart.setTotalPrice(BigDecimal.ZERO);
                cart.setShippingFee(BigDecimal.ZERO);
            }
        } else {
            cartItem.updateSubtotal();
        }

        cart.updateTotalPrice();
        log.info("Quantity decreased for item: {}", itemId);

        return cartItemMapper.toUpdateResponse(cartItem);
    }

    /**
     * Applies a discount coupon to the user's cart.
     *
     * @param code   The unique coupon code.
     * @param userId The ID of the user.
     */
    @Transactional
    public void applyCoupon(String code, Long userId) {
        log.info("Applying coupon: {} for user: {}", code, userId);
        checkUserIdIsNotNull(userId);

        // Retrieve coupon and validate its existence
        var coupon = couponService.findCouponOrThrow(code.trim());
        Cart cart = findCartOrThrow(userId);

        // Validate if the coupon is applicable to the current cart state
        checkCouponIsReadyForUse(coupon, cart);

        cart.setCoupon(coupon);
        cart.updateTotalPrice();
        log.info("Coupon: {} applied successfully to cart: {}", code, cart.getId());
    }

    /**
     * Removes the currently applied coupon from the user's cart.
     *
     * @param userId The ID of the user.
     */
    @Transactional
    public void removeCoupon(Long userId) {
        checkUserIdIsNotNull(userId);
        Cart cart = findCartOrThrow(userId);

        if (cart.getCoupon() == null) {
            throw new InvalidArgumentException("No coupon applied to this cart");
        }

        cart.setCoupon(null);
        cart.updateTotalPrice();
        log.info("Coupon removed successfully for user: {}", userId);
    }

    /**
     * Removes a specific item from the user's cart.
     *
     * @param itemId The ID of the cart item to delete.
     * @param userId The ID of the user owning the cart.
     */
    @Transactional
    public void deleteItem(Long itemId, Long userId) {
        log.info("Deleting item: {} from cart for user: {}", itemId, userId);
        checkUserIdIsNotNull(userId);

        Cart cart = findCartOrThrow(userId);
        CartItem item = findCartItemOrThrow(itemId);
        checkCartIdMatch(cart, item);

        cart.getItems().remove(item);
        cartItemRepository.delete(item);

        cart.updateTotalPrice();
        log.info("Item: {} successfully deleted and cart total updated", itemId);
    }

    /**
     * Removes all items from the user's cart and resets the total price.
     *
     * @param userId The ID of the user whose cart should be cleared.
     */
    @Transactional
    public void clearCart(Long userId) {
        checkUserIdIsNotNull(userId);
        Cart cart = findCartOrThrow(userId);

        cart.getItems().clear();

        cart.setShippingFee(BigDecimal.ZERO);
        cart.setTotalOriginalPrice(BigDecimal.ZERO);
        cart.setTotalPrice(BigDecimal.ZERO);
        cart.setShippingFee(BigDecimal.ZERO);
        cart.setCoupon(null);
        cart.setCouponApplied(false);

        cartRepository.save(cart);

        log.info("Cart cleared for user {}", userId);
    }

    /**
     * Finds a cart by user ID or throws an exception if not found.
     *
     * @param userId The ID of the user.
     * @return The found Cart entity.
     */
    private Cart findCartOrThrow(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Cart not found with user id: {}", userId);
                    return new CartNotFoundException("Cart not found");
                });
    }

    /**
     * Finds a cart item by its ID or throws an exception if not found.
     *
     * @param id The ID of the cart item.
     * @return The found CartItem entity.
     */
    private CartItem findCartItemOrThrow(Long id) {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cart item not found with id: {}", id);
                    return new CartItemNotFoundException("Cart item not found");
                });
    }

    /**
     * Validates that the user ID is not null.
     *
     * @param userId The ID of the user to check.
     */
    private void checkUserIdIsNotNull(Long userId) {
        if (userId == null) {
            throw new UnauthorizedException("Unauthorized user");
        }
    }

    /**
     * Validates that a specific cart item belongs to the given cart.
     *
     * @param cart     The cart entity.
     * @param cartItem The cart item entity.
     */
    private void checkCartIdMatch(Cart cart, CartItem cartItem) {
        if (!Objects.equals(cart.getId(), cartItem.getCart().getId())) {
            throw new InvalidArgumentException("Cart item does not belong to the cart");
        }
    }

    /**
     * Validates coupon status, expiration, usage limits, and minimum order requirements.
     *
     * @param coupon The coupon entity to validate.
     * @param cart   The cart entity to check against.
     */
    private void checkCouponIsReadyForUse(Coupon coupon, Cart cart) {
        if (!coupon.isActive()) {
            log.error("Coupon is not active for item: {}", coupon.getId());
            throw new CouponIsNotActiveException("Coupon is not active");
        }

        if (coupon.getExpiryDate().isBefore(LocalDate.now())) {
            log.error("Coupon is expired for item: {}", coupon.getId());
            throw new CouponExpiredException("Coupon is expired");
        }

        if (coupon.getUsedCount() >= coupon.getUsageLimit()) {
            log.error("Coupon used limit for item: {}", coupon.getId());
            throw new CouponUsedLimitException("Coupon used limit");
        }

        if (cart.getTotalPrice().compareTo(coupon.getMinOrderAmount()) < 0) {
            log.error("Cart total price is less than minimum order amount for coupon: {}", coupon.getId());
            throw new CouponMinAmountException("Total price is less than " + coupon.getMinOrderAmount() + "AZN");
        }
    }

}
