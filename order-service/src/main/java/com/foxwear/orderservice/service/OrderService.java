package com.foxwear.orderservice.service;

import com.foxwear.common.exception.InvalidArgumentException;
import com.foxwear.common.exception.UnauthorizedException;
import com.foxwear.orderservice.dto.request.OrderCreateRequest;
import com.foxwear.orderservice.dto.request.OrderFilterRequest;
import com.foxwear.orderservice.dto.response.*;
import com.foxwear.orderservice.entity.Order;
import com.foxwear.orderservice.entity.OrderItem;
import com.foxwear.orderservice.enums.OrderStatus;
import com.foxwear.orderservice.enums.PaymentMethod;
import com.foxwear.orderservice.enums.PaymentStatus;
import com.foxwear.orderservice.exception.OrderNotFoundException;
import com.foxwear.orderservice.exception.PaymentException;
import com.foxwear.orderservice.exception.UnpaidException;
import com.foxwear.orderservice.mapper.OrderItemMapper;
import com.foxwear.orderservice.mapper.OrderMapper;
import com.foxwear.orderservice.repository.OrderRepository;
import com.foxwear.orderservice.repository.specification.OrderSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final PaymentService paymentService;
    private final CouponService couponService;
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

        // Validate that the cart is not empty before proceeding
        if (cart.getItems().isEmpty()) {
            throw new InvalidArgumentException("Cannot create order with an empty cart");
        }

        if (request.getPaymentMethod() == PaymentMethod.CARD) {
            if (request.getCardNumber() == null || request.getExpiryMonth() == null || request.getExpiryYear() == null || request.getCvc() == null) {
                throw new InvalidArgumentException("Card details are required for card payments");
            }

            log.info("Processing card payment for user: {} amount: {}", userId, cart.getTotalPrice());
            boolean paymentSuccess = paymentService.process(cart.getTotalPrice(), request);

            if (!paymentSuccess) {
                throw new PaymentException("Payment failed");
            }
        }
        log.info("Payment validation passed for user: {}", userId);

        PaymentStatus initialPaymentStatus = (request.getPaymentMethod() == PaymentMethod.CARD)
                ? PaymentStatus.PAID
                : PaymentStatus.UNPAID;

        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .userId(userId)
                .status(OrderStatus.PENDING)
                .paymentStatus(initialPaymentStatus)
                .paymentMethod(request.getPaymentMethod())
                .addressSnapshot(request.getAddressSnapshot())
                .latitudeSnapshot(request.getLatitude())
                .longitudeSnapshot(request.getLongitude())
                .orderNote(request.getOrderNote())
                .phoneNumber(request.getPhoneNumber())
                .totalDiscountPrice(cart.getTotalPrice())
                .build();

        // Map cart items to order items and link them to the order
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> mapToOrderItem(cartItem, order))
                .toList();
        order.setItems(orderItems);

        // Persist the order to the database
        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully. Order Number: {}", savedOrder.getOrderNumber());

        // Post-creation cleanup: clear cart and update coupon usage
        if (cart.getCoupon() != null) {
            couponService.increaseUsedCount(cart.getCoupon().getId());
        }
        cartService.clearCart(userId);
        return mapToOrderCreateResponse(savedOrder);
    }

    /**
     * Retrieves all orders that are currently in PENDING status.
     *
     * @param status The status to filter orders by
     * @return A list of OrderCreateResponse DTOs
     */
    @Transactional(readOnly = true)
    public List<OrderGetAllResponse> getOrdersByStatus(OrderStatus status) {
        log.info("Fetching all orders with status: {}", status);
        return orderRepository.findAllByStatus(status)
                .stream()
                .map(orderMapper::toGetAllResponse)
                .toList();
    }

    /**
     * Retrieves a specific order with details optimized for courier view.
     *
     * @param id     The internal ID of the order
     * @param userId The ID of the courier requesting the order
     * @return OrderGetCourierResponse containing delivery-specific details
     */
    @Transactional(readOnly = true)
    public OrderGetCourierResponse getOrderByIdWithCourierResponse(Long id, Long userId) {
        log.info("Fetching order with id {} for courier: {}", id, userId);
        checkUserIdIsNotNull(userId);

        Order order = findOrderOrThrow(id);

        return mapToOrderGetCourierResponse(order);
    }

    /**
     * Retrieves all orders currently in SHIPPED status assigned to a specific courier.
     *
     * @param courierId The ID of the courier
     * @return A list of OrderGetAllResponse DTOs representing the courier's tasks
     */
    public List<OrderGetAllResponse> getCourierTasks(Long courierId) {
        log.info("Fetching tasks for courier: {}", courierId);
        checkUserIdIsNotNull(courierId);

        List<Order> orders = orderRepository.findAllByCourierIdAndStatus(courierId, OrderStatus.SHIPPED);

        if (orders.isEmpty()) {
            log.info("No tasks found for courier: {}", courierId);
            return Collections.emptyList();
        }

        return orders.stream()
                .map(orderMapper::toGetAllResponse)
                .toList();
    }

    /**
     * Retrieves a paginated list of delivered orders for a specific courier.
     *
     * @param courierId The ID of the courier
     * @param page      The page number to retrieve
     * @param size      The number of records per page
     * @return A page of OrderGetAllResponse DTOs
     */
    public Page<OrderGetAllResponse> getMyDeliveredOrders(Long courierId, Integer page, Integer size) {
        log.info("Fetching delivered orders for courier: {}, page: {}, size: {}", courierId, page, size);
        checkUserIdIsNotNull(courierId);

        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderRepository.findAllByCourierIdAndStatusOrderByDeliveredAtDesc(courierId, OrderStatus.DELIVERED, pageable);

        return orders.map(orderMapper::toGetAllResponse);
    }

    /**
     * Retrieves all orders belonging to a specific user.
     *
     * @param userId The ID of the user whose orders are being retrieved
     * @return A list of OrderGetAllResponse DTOs
     */
    @Transactional(readOnly = true)
    public List<OrderGetAllResponse> getMyOrders(Long userId) {
        log.info("Fetching all orders for user: {}", userId);
        checkUserIdIsNotNull(userId);

        List<Order> orders = orderRepository.findAllByUserId(userId);

        if (orders.isEmpty()) {
            log.info("No orders found for user: {}", userId);
            return Collections.emptyList();
        }

        return orders.stream()
                .map(orderMapper::toGetAllResponse)
                .toList();
    }

    /**
     * Retrieves a specific order by its order number for a given user.
     *
     * @param orderNumber The unique order number
     * @param userId      The ID of the user requesting the order
     * @return OrderCreateResponse containing the order details
     */
    @Transactional(readOnly = true)
    public OrderGetResponse getOrderByOrderNumber(String orderNumber, Long userId) {
        log.info("Fetching order with number {} for user: {}", orderNumber, userId);
        checkUserIdIsNotNull(userId);

        Order order = findOrderOrThrow(orderNumber);

        if (!order.getUserId().equals(userId)) {
            log.warn("User {} attempted to access order {} belonging to user {}", userId, orderNumber, order.getUserId());
            throw new UnauthorizedException("You do not have permission to view this order");
        }

        return mapToOrderGetResponse(order);
    }

    /**
     * Retrieves a specific order by its internal ID.
     *
     * @param id     The internal ID of the order
     * @param userId The ID of the user requesting the order
     */
    @Transactional(readOnly = true)
    public OrderGetResponse getOrderById(Long id, Long userId) {
        log.info("Fetching order with id {} for user: {}", id, userId);
        checkUserIdIsNotNull(userId);

        Order order = findOrderOrThrow(id);

        return mapToOrderGetResponse(order);
    }

    /**
     * Retrieves a paginated list of orders based on various filters.
     *
     * @param filter The filtering and pagination criteria
     * @return A page of OrderGetAllResponse DTOs
     */
    @Transactional(readOnly = true)
    public Page<OrderGetAllResponse> getAllOrders(OrderFilterRequest filter) {
        log.info("Fetching paginated orders with filters: {}", filter);
        Pageable pageable = PageRequest.of(
                filter.getPage(),
                filter.getSize(),
                Sort.by(filter.getDirection(), filter.getSortBy())
        );

        Page<Order> orders = orderRepository.findAll(buildOrderSpecification(filter), pageable);

        return orders.map(orderMapper::toGetAllResponse);
    }

    /**
     * Retrieves all possible order statuses.
     *
     * @return List of status names
     */
    @Transactional(readOnly = true)
    public List<String> getOrderStatuses() {
        log.info("Fetching all available order statuses");
        return Arrays.stream(OrderStatus.values())
                .map(OrderStatus::name)
                .toList();
    }

    /**
     * Retrieves all possible payment statuses.
     *
     * @return List of payment status names
     */
    @Transactional(readOnly = true)
    public List<String> getPaymentStatuses() {
        log.info("Fetching all available payment statuses");
        return Arrays.stream(PaymentStatus.values())
                .map(PaymentStatus::name)
                .toList();
    }

    /**
     * Retrieves all possible payment methods.
     *
     * @return List of payment method names
     */
    @Transactional(readOnly = true)
    public List<String> getPaymentMethods() {
        log.info("Fetching all available payment methods");
        return Arrays.stream(PaymentMethod.values())
                .map(PaymentMethod::name)
                .toList();
    }

    /**
     * Updates the order status to PREPARING.
     *
     * @param orderId The ID of the order to update
     * @param adminId The ID of the admin performing the action
     */
    @Transactional
    public void setPreparingOrder(Long orderId, Long adminId) {
        log.info("Admin {} is setting order {} to PREPARING status", adminId, orderId);
        checkUserIdIsNotNull(adminId);
        Order order = findOrderOrThrow(orderId);

        // Ensure payment is secured for non-COD orders
        if (order.getPaymentMethod() != PaymentMethod.CASH_ON_DELIVERY && order.getPaymentStatus() != PaymentStatus.PAID) {
            throw new UnpaidException("The order cannot be processed because the online payment was unsuccessful.");
        }

        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.PREPARING);
            log.info("Order {} status updated to PREPARING", orderId);
        } else
            throw new InvalidArgumentException("Cannot set preparing order");
    }

    /**
     * Updates the order status to READY_FOR_PICKUP and records the preparation time.
     *
     * @param orderId The ID of the order to update
     * @param adminId The ID of the admin performing the action
     */
    @Transactional
    public void setPreparedOrder(Long orderId, Long adminId) {
        log.info("Admin {} is setting order {} to READY_FOR_PICKUP status", adminId, orderId);
        checkUserIdIsNotNull(adminId);
        Order order = findOrderOrThrow(orderId);

        if (order.getStatus() == OrderStatus.PREPARING) {
            order.setStatus(OrderStatus.READY_FOR_PICKUP);
            order.setPreparedAt(LocalDateTime.now());
        } else {
            throw new InvalidArgumentException("Cannot set prepared order");
        }
    }

    /**
     * Assigns a courier to the order and updates status to SHIPPED.
     *
     * @param orderId   The ID of the order
     * @param courierId The ID of the courier to assign
     */
    @Transactional
    public void assignCourier(Long orderId, Long courierId) {
        log.info("Assigning courier {} to order {}", courierId, orderId);
        checkUserIdIsNotNull(courierId);
        Order order = findOrderOrThrow(orderId);

        if (order.getCourierId() != null) {
            log.warn("Assignment failed: Order {} already has courier {}", orderId, order.getCourierId());
            throw new InvalidArgumentException("Order already has a courier assigned");
        }

        if (order.getStatus() == OrderStatus.READY_FOR_PICKUP) {
            order.setCourierId(courierId);
            order.setStatus(OrderStatus.SHIPPED);
            order.setPickedUpAt(LocalDateTime.now());
            log.info("Order {} successfully assigned to courier and status updated to SHIPPED", orderId);
        } else {
            throw new InvalidArgumentException("Cannot assign courier to order");
        }
    }

    /**
     * Marks the order as DELIVERED and updates payment status.
     *
     * @param orderId The ID of the order to deliver
     */
    @Transactional
    public void deliverOrder(Long orderId, Long courierId) {
        log.info("Marking order {} as DELIVERED", orderId);
        checkUserIdIsNotNull(courierId);
        Order order = findOrderOrThrow(orderId);

        if (order.getStatus() == OrderStatus.SHIPPED) {
            order.setStatus(OrderStatus.DELIVERED);
            order.setPaymentStatus(PaymentStatus.PAID);
            order.setDeliveredAt(LocalDateTime.now());
            log.info("Order {} successfully delivered", orderId);
        } else {
            throw new InvalidArgumentException("Cannot deliver order");
        }
    }

    /**
     * Updates the status of an order to a specific value and manages related timestamps.
     *
     * @param orderId The ID of the order to update
     * @param status  The new status to apply
     */
    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = findOrderOrThrow(orderId);
        log.info("Updating status for order {} to {}", orderId, status);

        if (order.getStatus() == status) {
            throw new InvalidArgumentException("Order is already in " + status + " status");
        }

        switch (status) {
            case PENDING, PREPARING -> {
                order.setPreparedAt(null);
                order.setPickedUpAt(null);
                order.setDeliveredAt(null);
            }
            case READY_FOR_PICKUP -> {
                if (order.getPreparedAt() == null) {
                    order.setPreparedAt(LocalDateTime.now());
                }
                order.setPickedUpAt(null);
                order.setDeliveredAt(null);
            }
            case SHIPPED -> {
                if (order.getPickedUpAt() == null) {
                    order.setPickedUpAt(LocalDateTime.now());
                }
                order.setDeliveredAt(null);
            }

            case DELIVERED -> {
                if (order.getDeliveredAt() == null) {
                    order.setDeliveredAt(LocalDateTime.now());
                }
                order.setPaymentStatus(PaymentStatus.PAID);
            }
        }

        order.setStatus(status);
        log.info("Order {} status successfully updated to {}", orderId, status);
    }

    /**
     * Finds an order by its ID or throws an exception if not found.
     *
     * @param orderId The ID of the order to find
     * @return The found Order entity
     * @throws OrderNotFoundException if the order does not exist
     */
    private Order findOrderOrThrow(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order not found with ID: {}", orderId);
                    return new OrderNotFoundException("Order not found");
                });
    }

    /**
     * Finds an order by its unique order number or throws an exception if not found.
     *
     * @param orderNumber The unique order number string
     * @return The found Order entity
     */
    private Order findOrderOrThrow(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber.toUpperCase())
                .orElseThrow(() -> {
                    log.error("Order not found with number: {}", orderNumber);
                    return new OrderNotFoundException("Order not found");
                });
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
                .discountAtPurchase(cartItem.getOriginalSubTotal().subtract(cartItem.getSubTotal()))
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
     * Maps the Order entity and its items to a DTO response.
     *
     * @param order The saved Order entity
     * @return OrderCreateResponse DTO
     */
    private OrderCreateResponse mapToOrderCreateResponse(Order order) {
        OrderCreateResponse response = orderMapper.toCreateResponse(order);
        response.setItems(
                order.getItems().stream()
                        .map(orderItemMapper::toCreateResponse)
                        .toList()
        );

        return response;
    }

    /**
     * Maps the Order entity and its items to a detailed GET response DTO.
     *
     * @param order The Order entity to map
     * @return OrderGetResponse containing full order details and items
     */
    private OrderGetResponse mapToOrderGetResponse(Order order) {
        OrderGetResponse response = orderMapper.toGetResponse(order);
        response.setItems(
                order.getItems().stream()
                        .map(orderItemMapper::toGetResponse)
                        .toList()
        );

        return response;
    }

    /**
     * Maps the Order entity to a response DTO tailored for courier operations.
     *
     * @param order The Order entity to map
     * @return OrderGetCourierResponse containing summary and location data
     */
    private OrderGetCourierResponse mapToOrderGetCourierResponse(Order order) {
        OrderGetCourierResponse response = orderMapper.toGetCourierResponse(order);
        response.setItemsCount(order.getItems().size());

        return response;
    }

    /**
     * Validates that the user ID is not null.
     *
     * @param userId The user ID to check
     */
    private void checkUserIdIsNotNull(Long userId) {
        if (userId == null) {
            log.error("Security violation: Attempted operation with null user ID");
            throw new UnauthorizedException("Unauthorized user");
        }
    }

    /**
     * Builds a JPA Specification for filtering orders based on the request criteria.
     *
     * @param filter The filter request containing search terms and statuses
     * @return A Specification for the Order entity
     */
    private Specification<Order> buildOrderSpecification(OrderFilterRequest filter) {
        return Specification.where(OrderSpecification.hasPaymentMethod(filter.getPaymentMethods()))
                .and(OrderSpecification.hasPaymentStatus(filter.getPaymentStatuses()))
                .and(OrderSpecification.hasStatus(filter.getOrderStatuses()))
                .and(OrderSpecification.search(filter.getSearchKeyword()));
    }

}
