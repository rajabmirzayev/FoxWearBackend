package com.foxwear.interactionservice.service;

import com.foxwear.interactionservice.dto.response.ReviewGetAllResponse;
import com.foxwear.interactionservice.mapper.ReviewMapper;
import com.foxwear.interactionservice.repository.SiteReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing site reviews.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final SiteReviewRepository siteReviewRepository;
    private final ReviewMapper reviewMapper;

    /**
     * Retrieves the first 10 reviews from the database.
     *
     * @return a list of {@link ReviewGetAllResponse} containing review details.
     */
    public List<ReviewGetAllResponse> getFirst10Reviews() {
        log.info("Fetching the first 10 reviews from the database");

        var response = siteReviewRepository.findAllFirst10(PageRequest.of(0, 10));

        return response.stream()
                .map(reviewMapper::toGetAllResponse)
                .toList();
    }
}
