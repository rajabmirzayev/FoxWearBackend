package com.foxwear.authservice.service;

import com.foxwear.authservice.dto.response.UserStatsResponse;
import com.foxwear.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {
    private final UserRepository userRepository;

    /**
     * Retrieves statistics for the user dashboard, including new users in the last 30 days
     * and the growth percentage compared to the previous 30-day period.
     *
     * @return UserStatsResponse containing user count and growth percentage.
     */
    @Transactional(readOnly = true)
    public UserStatsResponse getUserDashboardStats() {
        log.info("Fetching user dashboard statistics");

        // Define time ranges for current and previous 30-day periods
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDateTime thirtyDaysAgo = now.minusDays(30);
        LocalDateTime sixtyDaysAgo = now.minusDays(60);

        long currentPeriodUsers = userRepository.countByCreatedAtBetween(thirtyDaysAgo, now);
        long previousPeriodUsers = userRepository.countByCreatedAtBetween(sixtyDaysAgo, thirtyDaysAgo);

        double growth = calculateGrowth(currentPeriodUsers, previousPeriodUsers);

        log.debug("Stats calculated - Current: {}, Previous: {}, Growth: {}%",
                currentPeriodUsers, previousPeriodUsers, growth);

        return UserStatsResponse.builder()
                .newUsersCount(currentPeriodUsers)
                .growthPercentage(growth)
                .build();
    }

    /**
     * Calculates the percentage growth between two periods.
     *
     * @return Rounded growth percentage.
     */
    private double calculateGrowth(long current, long previous) {
        if (previous == 0) {
            return current > 0 ? 100.0 : 0.0;
        }

        double growth = ((double) (current - previous) / previous) * 100;

        return Math.round(growth * 10.0) / 10.0;
    }

}
