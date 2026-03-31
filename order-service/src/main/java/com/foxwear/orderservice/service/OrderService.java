package com.foxwear.orderservice.service;

import com.foxwear.common.exception.InvalidArgumentException;
import com.foxwear.common.exception.UnauthorizedException;
import com.foxwear.orderservice.dto.request.OrderCreateRequest;
import com.foxwear.orderservice.dto.response.CartGetResponse;
import com.foxwear.orderservice.dto.response.CartItemGetResponse;
import com.foxwear.orderservice.dto.response.OrderCreateResponse;
import com.foxwear.orderservice.entity.Order;
import com.foxwear.orderservice.entity.OrderItem;
import com.foxwear.orderservice.enums.OrderStatus;
import com.foxwear.orderservice.enums.PaymentStatus;
import com.foxwear.orderservice.mapper.OrderItemMapper;
import com.foxwear.orderservice.mapper.OrderMapper;
import com.foxwear.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    /**
     * Creates a new order based on the user's current cart contents.
     *
     * @param request The order creation details (payment method, address, etc.)
     * @param userId  The ID of the user placing the order
     * @return OrderCreateResponse containing the details of the created order
     */
    @Transactional
    public OrderCreateResponse createOrder(OrderCreateRequest request, Long userId) {
        log.info("Starting order creation for user: {}", userId);
        checkUserIdIsNotNull(userId);
        CartGetResponse cart = cartService.getCart(userId);

        if (cart.getItems().isEmpty()) {
            throw new InvalidArgumentException("Cannot create order with an empty cart");
        }

        BigDecimal shippingFee = calculateShippingFee(cart.getTotalPrice());
        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .userId(userId)
                .status(OrderStatus.PENDING)
                .paymentStatus(PaymentStatus.UNPAID)
                .paymentMethod(request.getPaymentMethod())
                .addressSnapshot(request.getAddressSnapshot())
                .latitudeSnapshot(request.getLatitude())
                .longitudeSnapshot(request.getLongitude())
                .orderNote(request.getOrderNote())
                .couponId(request.getCouponId())
                .shippingFee(shippingFee)
                .totalDiscountPrice(cart.getTotalPrice().add(shippingFee))
                .build();

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> mapToOrderItem(cartItem, order))
                .toList();
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully. Order Number: {}", savedOrder.getOrderNumber());

        cartService.clearCart(userId);
        return mapToOrderResponse(savedOrder);
    }

    /**
     * Maps a cart item to an order item entity.
     *
     * @param cartItem The cart item to map
     * @param order    The order to which this item belongs
     * @return A populated OrderItem entity
     */
    private OrderItem mapToOrderItem(CartItemGetResponse cartItem, Order order) {
        return OrderItem.builder()
                .order(order)
                .productItemId(cartItem.getProductItemId())
                .productName(cartItem.getProductName())
                .colorName(cartItem.getColorName())
                .imageUrl(cartItem.getImageUrl())
                .sizeValue(cartItem.getSizeValue())
                .slug(cartItem.getSlug())
                .quantity(cartItem.getQuantity())
                .priceAtPurchase(cartItem.getOriginalUnitPrice())
                .subTotal(cartItem.getSubTotal())
                .discountAtPurchase(
                        Objects.equals(
                                cartItem.getOriginalUnitPrice(),
                                cartItem.getActualUnitPrice()
                        )
                                ? BigDecimal.ZERO
                                : cartItem.getOriginalUnitPrice().subtract(cartItem.getActualUnitPrice())
                )
                .build();
    }

    /**
     * Generates a unique order number based on the current timestamp.
     *
     * @return A string representing the unique order number
     */
    private String generateOrderNumber() {
        return "FW-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }

    /**
     * Calculates the shipping fee based on the total order amount.
     *
     * @param totalAmount The total price of items in the cart
     * @return The calculated shipping fee
     */
    private BigDecimal calculateShippingFee(BigDecimal totalAmount) {
        return totalAmount.compareTo(BigDecimal.valueOf(70)) >= 0
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(5.00);
    }

    /**
     * Maps the Order entity and its items to a DTO response.
     *
     * @param order The saved Order entity
     * @return OrderCreateResponse DTO
     */
    private OrderCreateResponse mapToOrderResponse(Order order) {
        OrderCreateResponse response = orderMapper.toCreateResponse(order);
        response.setItems(
                order.getItems().stream()
                        .map(orderItemMapper::toCreateResponse)
                        .toList()
        );

        return response;
    }

    /**
     * Validates that the user ID is not null.
     *
     * @param userId The user ID to check
     */
    private void checkUserIdIsNotNull(Long userId) {
        if (userId == null) {
            throw new UnauthorizedException("Unauthorized user");
        }
    }
}
