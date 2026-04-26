package com.foxwear.orderservice.repository.specification;

import com.foxwear.orderservice.entity.Order;
import com.foxwear.orderservice.entity.OrderItem;
import com.foxwear.orderservice.enums.OrderStatus;
import com.foxwear.orderservice.enums.PaymentMethod;
import com.foxwear.orderservice.enums.PaymentStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class OrderSpecification {

    public static Specification<Order> hasStatus(List<OrderStatus> statuses) {
        return ((root, query, cb) ->
                statuses == null || statuses.isEmpty() ? null : root.get("status").in(statuses));
    }

    public static Specification<Order> hasPaymentStatus(List<PaymentStatus> paymentStatuses) {
        return ((root, query, cb) ->
                paymentStatuses == null || paymentStatuses.isEmpty() ? null : root.get("paymentStatus").in(paymentStatuses));
    }

    public static Specification<Order> hasPaymentMethod(List<PaymentMethod> paymentMethods) {
        return ((root, query, cb) ->
                paymentMethods == null || paymentMethods.isEmpty() ? null : root.get("paymentMethod").in(paymentMethods));
    }

    public static Specification<Order> search(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;

            String pattern = "%" + keyword.trim().toLowerCase() + "%";

            Predicate orderNumberPredicate = cb.like(cb.lower(root.get("orderNumber")), pattern);
            Predicate trackingNumberPredicate = cb.like(cb.lower(root.get("trackingNumber")), pattern);

            Join<Order, OrderItem> items = root.join("items");
            Predicate slugPredicate = cb.like(cb.lower(items.get("slug")), pattern);
            Predicate namePredicate = cb.like(cb.lower(items.get("productName")), pattern);

            if (query != null) query.distinct(true);

            return cb.or(orderNumberPredicate, trackingNumberPredicate, slugPredicate, namePredicate);
        };
    }

}
