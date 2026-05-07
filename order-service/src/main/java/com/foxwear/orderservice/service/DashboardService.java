package com.foxwear.orderservice.service;

import com.foxwear.common.dto.response.ProductItemResponse;
import com.foxwear.common.dto.response.ProductResponse;
import com.foxwear.orderservice.client.ProductClient;
import com.foxwear.orderservice.dto.response.DashboardSummaryResponse;
import com.foxwear.orderservice.dto.response.SalesDataDTO;
import com.foxwear.orderservice.dto.response.TopProductResponse;
import com.foxwear.orderservice.repository.OrderRepository;
import com.foxwear.orderservice.repository.SaleItemRepository;
import com.foxwear.orderservice.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {
    private final OrderRepository orderRepository;
    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;
    private final ProductClient productClient;

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
     * Retrieves combined sales and order statistics for the last 7 days.
     *
     * @return List of SalesDataDTO containing daily revenue totals.
     */
    @Transactional(readOnly = true)
    public List<SalesDataDTO> getCombinedSalesOverview() {
        log.info("Fetching combined sales and order statistics for the last 7 days");

        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(6).withHour(0).withMinute(0).withSecond(0).withNano(0);

        List<SalesDataDTO> orderStats = orderRepository.getDailyStats(sevenDaysAgo);
        List<SalesDataDTO> saleStats = saleRepository.getDailyStats(sevenDaysAgo);

        // Initialize map with last 7 days to ensure all days are present even with zero revenue
        Map<LocalDate, BigDecimal> combinedMap = new TreeMap<>();

        for (int i = 0; i <= 6; i++) {
            combinedMap.put(LocalDate.now().minusDays(i), BigDecimal.ZERO);
        }

        log.debug("Processing and merging statistics from orders and sales");
        // Merge data from both repositories into the map
        processStats(orderStats, combinedMap);
        processStats(saleStats, combinedMap);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH);

        return combinedMap.entrySet().stream()
                .map(entry -> new SalesDataDTO(
                        entry.getKey().format(formatter),
                        entry.getValue()))
                .toList();
    }

    /**
     * Retrieves the top-selling products by aggregating data from both online orders and physical sales.
     * Fetches product details (name, image) from the Product Microservice.
     *
     * @param limit The maximum number of top products to return.
     * @return A list of TopProductResponse containing sales volume and product details.
     */
    @Transactional(readOnly = true)
    public List<TopProductResponse> getTopProducts(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        Map<Long, TopProductResponse> combinedMap = new HashMap<>();

        orderRepository.getTopSellingProductIdsFromOrders(pageable).forEach(row -> {
            Long id = (Long) row[0];
            combinedMap.put(id, TopProductResponse.builder()
                    .productItemId(id)
                    .totalQuantity(((Long) row[1]).intValue())
                    .totalRevenue((BigDecimal) row[2])
                    .build());
        });

        saleItemRepository.getTopSellingProductIdsFromSales(pageable).forEach(row -> {
            Long id = (Long) row[0];
            Integer qty = ((Long) row[1]).intValue();
            BigDecimal revenue = (BigDecimal) row[2];

            combinedMap.merge(id,
                    TopProductResponse.builder().productItemId(id).totalQuantity(qty).totalRevenue(revenue).build(),
                    (oldV, newV) -> {
                        oldV.setTotalQuantity(oldV.getTotalQuantity() + newV.getTotalQuantity());
                        oldV.setTotalRevenue(oldV.getTotalRevenue().add(newV.getTotalRevenue()));
                        return oldV;
                    });
        });

        List<Long> productItemIds = combinedMap.keySet().stream().toList();

        Map<Long, ProductItemResponse> detailsMap = productClient.getProductItemsByIds(productItemIds).getData()
                .stream().collect(Collectors.toMap(ProductItemResponse::getId, d -> d));

        return combinedMap.values().stream()
                .peek(dto -> {
                    ProductItemResponse detail = detailsMap.get(dto.getProductItemId());
                    if (detail != null) {
                        dto.setProductName(detail.getTitle());
                        dto.setImageUrl(detail.getImageUrl());
                        dto.setProductSlug(detail.getProductSlug());
                    }
                })
                .sorted(Comparator.comparing(TopProductResponse::getTotalQuantity).reversed())
                .limit(limit)
                .toList();
    }

    /**
     * Processes raw statistics and merges them into the provided map.
     *
     * @param stats List of raw sales data from repository
     * @param map   The target map to accumulate values
     */
    private void processStats(List<SalesDataDTO> stats, Map<LocalDate, BigDecimal> map) {
        if (stats == null) return;

        for (SalesDataDTO stat : stats) {
            if (stat.getDay() != null) {
                LocalDate date = LocalDate.parse(stat.getDay().trim());
                if (map.containsKey(date)) {
                    map.merge(date, stat.getAmount(), BigDecimal::add);
                }
            }
        }
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
