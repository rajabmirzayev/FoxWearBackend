package com.foxwear.orderservice.service;

import com.foxwear.orderservice.dto.response.DashboardSummaryResponse;
import com.foxwear.orderservice.repository.OrderRepository;
import com.foxwear.orderservice.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {
    private final OrderRepository orderRepository;
    private final SaleRepository saleRepository;

    /**
     * Retrieves a summary of business performance including revenue and order growth.
     *
     * @return DashboardSummaryResponse containing total metrics and percentage growth.
     */
    @Transactional(readOnly = true)
    public DashboardSummaryResponse getOverview() {
        log.info("Calculating dashboard overview metrics");

        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDateTime thirtyDaysAgo = now.minusDays(30);
        LocalDateTime sixtyDaysAgo = now.minusDays(60);

        BigDecimal currentRevenue = getTotalRevenueInRange(thirtyDaysAgo, now);
        BigDecimal previousRevenue = getTotalRevenueInRange(sixtyDaysAgo, thirtyDaysAgo);

        long currentOrders = orderRepository.countOrdersByDateRange(thirtyDaysAgo, now);
        long previousOrders = orderRepository.countOrdersByDateRange(sixtyDaysAgo, thirtyDaysAgo);

        return DashboardSummaryResponse.builder()
                .totalRevenue(getTotalRevenueInRange(null, null))
                .revenueGrowthPercentage(calculateGrowth(currentRevenue, previousRevenue))
                .totalOrders(orderRepository.count())
                .ordersGrowthPercentage(calculateGrowth(
                        BigDecimal.valueOf(currentOrders),
                        BigDecimal.valueOf(previousOrders)))
                .build();
    }

    /**
     * Calculates total revenue by summing up values from Order and Sale repositories.
     *
     * @param start Start date of the range (optional)
     * @param end   End date of the range (optional)
     * @return Total revenue as BigDecimal
     */
    private BigDecimal getTotalRevenueInRange(LocalDateTime start, LocalDateTime end) {
        BigDecimal orders;
        BigDecimal sales;

        if (start == null) {
            orders = Optional.ofNullable(orderRepository.sumTotalRevenue()).orElse(BigDecimal.ZERO);
            sales = Optional.ofNullable(saleRepository.sumTotalRevenue()).orElse(BigDecimal.ZERO);
        } else {
            orders = Optional.ofNullable(orderRepository.sumRevenueByDateRange(start, end)).orElse(BigDecimal.ZERO);
            sales = Optional.ofNullable(saleRepository.sumRevenueByDateRange(start, end)).orElse(BigDecimal.ZERO);
        }
        return orders.add(sales);
    }

    /**
     * Calculates the percentage growth between two periods.
     *
     * @param current  The value for the current period
     * @param previous The value for the previous period
     * @return Growth percentage rounded to one decimal place
     */
    private double calculateGrowth(BigDecimal current, BigDecimal previous) {
        if (previous == null || previous.compareTo(BigDecimal.ZERO) == 0) {
            return current != null && current.compareTo(BigDecimal.ZERO) > 0 ? 100.0 : 0.0;
        }

        BigDecimal difference = current.subtract(previous);

        BigDecimal growth = difference.divide(previous, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        return Math.round(growth.doubleValue() * 10.0) / 10.0;
    }

}
