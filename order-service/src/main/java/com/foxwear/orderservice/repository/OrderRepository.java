package com.foxwear.orderservice.repository;

import com.foxwear.orderservice.dto.response.SalesDataDTO;
import com.foxwear.orderservice.entity.Order;
import com.foxwear.orderservice.enums.OrderStatus;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @NullMarked
    @EntityGraph(attributePaths = {"items"})
    Optional<Order> findById(Long id);

    @Query("SELECT p FROM Order p WHERE p.status = :orderStatus ORDER BY p.createdAt DESC")
    List<Order> findAllByStatus(@Param("orderStatus") OrderStatus orderStatus);

    List<Order> findAllByUserId(Long userId);

    @EntityGraph(attributePaths = {"items"})
    Optional<Order> findByOrderNumber(String orderNumber);

    Page<Order> findAll(Specification<Order> specification, Pageable pageable);

    List<Order> findAllByCourierIdAndStatus(Long courierId, OrderStatus status);

    Page<Order> findAllByCourierIdAndStatusOrderByDeliveredAtDesc(Long courierId, OrderStatus status, Pageable pageable);

    @Query("SELECT SUM(o.totalDiscountPrice) FROM Order o WHERE o.paymentStatus = 'PAID'")
    BigDecimal sumTotalRevenue();

    @Query("SELECT SUM(o.totalDiscountPrice) FROM Order o WHERE o.paymentStatus = 'PAID' AND o.createdAt BETWEEN :start AND :end")
    BigDecimal sumRevenueByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.createdAt >= :start AND o.createdAt < :end")
    long countOrdersByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT new com.foxwear.orderservice.dto.response.SalesDataDTO(" +
            "CAST(o.createdAt AS date), SUM(o.totalDiscountPrice)) " +
            "FROM Order o " +
            "WHERE o.createdAt >= :startDate " +
            "GROUP BY CAST(o.createdAt AS date)")
    List<SalesDataDTO> getDailyStats(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT oi.productItemId, SUM(oi.quantity), SUM(oi.subTotal) " +
            "FROM OrderItem oi " +
            "GROUP BY oi.productItemId " +
            "ORDER BY SUM(oi.quantity) DESC")
    List<Object[]> getTopSellingProductIdsFromOrders(Pageable pageable);

}
