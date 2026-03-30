package com.foxwear.orderservice.service;

import com.foxwear.orderservice.dto.request.CartItemCreateRequest;
import com.foxwear.orderservice.dto.response.CartGetResponse;
import com.foxwear.orderservice.dto.response.CartItemCreateResponse;
import com.foxwear.orderservice.entity.Cart;
import com.foxwear.orderservice.entity.CartItem;
import com.foxwear.orderservice.exception.CartNotFoundException;
import com.foxwear.orderservice.mapper.CartItemMapper;
import com.foxwear.orderservice.mapper.CartMapper;
import com.foxwear.orderservice.repository.CartItemRepository;
import com.foxwear.orderservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;

    @Transactional
    public void createCart(Long userId) {
        log.info("Creating cart...");
        Cart cart = new Cart();
        cart.setUserId(userId);
        cartRepository.save(cart);
        log.info("Cart created successfully");
    }

    @Transactional
    public CartItemCreateResponse addItemToCart(CartItemCreateRequest request, Long userId) {
        Cart cart = findCartOrThrow(userId);

        CartItem item = cartItemMapper.toEntity(request);
        item.setCart(cart);
        item.setActualUnitPrice(
                item.getActualUnitPrice() == null
                        ? item.getOriginalUnitPrice()
                        : item.getActualUnitPrice()
        );
        item.setQuantity(item.getQuantity() == null ? 0 : item.getQuantity());
        item.setSubTotal(
                item.getActualUnitPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity()))
                        .setScale(2, RoundingMode.HALF_UP)
        );

        var savedItem = cartItemRepository.save(item);
        cart.updateTotalPrice();
        return cartItemMapper.toCreateResponse(savedItem);
    }

    @Transactional(readOnly = true)
    public CartGetResponse getCart(Long userId) {
        Cart cart = findCartOrThrow(userId);

        CartGetResponse cartResponse = cartMapper.toGetResponse(cart);
        var itemsRes = cart.getItems().stream()
                .map(cartItemMapper::toGetResponse)
                .toList();
        cartResponse.setItems(itemsRes);

        return cartResponse;
    }

    private Cart findCartOrThrow(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Cart not found with user id: {}", userId);
                    return new CartNotFoundException("Cart not found");
                });
    }

}
