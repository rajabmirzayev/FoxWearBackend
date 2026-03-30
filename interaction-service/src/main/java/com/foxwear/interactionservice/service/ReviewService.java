package com.foxwear.interactionservice.service;

import com.foxwear.common.exception.InvalidArgumentException;
import com.foxwear.common.exception.UnauthorizedException;
import com.foxwear.interactionservice.dto.request.ProductReviewCreateRequest;
import com.foxwear.interactionservice.dto.request.ProductReviewUpdateRequest;
import com.foxwear.interactionservice.dto.request.SiteReviewCreateRequest;
import com.foxwear.interactionservice.dto.request.SiteReviewUpdateRequest;
import com.foxwear.interactionservice.dto.response.*;
import com.foxwear.interactionservice.entity.AbstractReview;
import com.foxwear.interactionservice.entity.ProductReview;
import com.foxwear.interactionservice.entity.SiteReview;
import com.foxwear.interactionservice.exception.ReviewIsNotActiveException;
import com.foxwear.interactionservice.exception.ReviewNotFoundException;
import com.foxwear.interactionservice.mapper.ReviewMapper;
import com.foxwear.interactionservice.repository.ProductReviewRepository;
import com.foxwear.interactionservice.repository.SiteReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * Service class for managing site reviews.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final SiteReviewRepository siteReviewRepository;
    private final ProductReviewRepository productReviewRepository;
    private final ReviewMapper reviewMapper;

    /**
     * Creates a new site review for a specific user.
     *
     * @param request the review details
     * @param userId  the ID of the user creating the review
     * @return the created review response
     */
    @Transactional
    public SiteReviewCreateResponse createSiteReview(SiteReviewCreateRequest request, Long userId) {
        log.info("Attempting to create site review for user: {}", userId);

        checkUserIdIsNotNull(userId);

        var review = reviewMapper.toSiteReviewEntity(request);
        review.setUserId(userId);

        var savedReview = siteReviewRepository.save(review);

        log.info("Successfully saved site review with ID: {} for user: {}", savedReview.getId(), userId);

        return reviewMapper.toSiteReviewCreateResponse(savedReview);
    }

    @Transactional
    public ProductReviewCreateResponse createProductReview(ProductReviewCreateRequest request, Long productId, Long userId) {
        log.info("Attempting to create product review for user: {}", userId);

        checkUserIdIsNotNull(userId);

        var review = reviewMapper.toProductReviewEntity(request);
        review.setUserId(userId);
        review.setProductId(productId);

        var savedReview = productReviewRepository.save(review);

        log.info("Successfully saved product review with ID: {} for user: {}", savedReview.getId(), userId);

        return reviewMapper.toProductReviewCreateResponse(savedReview);
    }

    /**
     * Retrieves a paginated list of all site reviews.
     *
     * @param page the page number to retrieve
     * @param size the number of records per page
     * @return a page of site review responses
     */
    @Transactional(readOnly = true)
    public Page<SiteReviewGetAllResponse> getAllSiteReviews(Integer page, Integer size) {
        log.info("Fetching paginated site reviews. Page: {}, Size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        var reviews = siteReviewRepository.findAllByIsActiveTrue(pageable);

        return reviews.map(reviewMapper::toSiteReviewGetAllResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProductReviewGetAllResponse> getAllProductReviews(Integer page, Integer size, Long productId) {
        log.info("Fetching paginated product reviews. Page: {}, Size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        var reviews = productReviewRepository.findAllByIsActiveTrue(pageable, productId);

        return reviews.map(reviewMapper::toProductReviewGetAllResponse);
    }

    /**
     * Retrieves all reviews created by a specific user.
     *
     * @param userId the ID of the user whose reviews are being fetched
     * @return a list of site review responses
     */
    @Transactional(readOnly = true)
    public List<SiteReviewGetAllResponse> getMySiteReviews(Long userId) {
        log.info("Fetching all reviews for user ID: {}", userId);
        if (userId == null) {
            log.warn("User ID is null, returning empty list of site reviews");
            return Collections.emptyList();
        }

        var reviews = siteReviewRepository.findAllByUserId(userId);
        log.info("Found {} site reviews for user ID: {}", reviews.size(), userId);

        return reviews.stream()
                .map(reviewMapper::toSiteReviewGetAllResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductReviewGetAllResponse> getMyProductReviews(Long userId) {
        log.info("Fetching all product reviews for user ID: {}", userId);
        if (userId == null) {
            log.warn("User ID is null, returning empty list of product reviews");
            return Collections.emptyList();
        }

        var reviews = productReviewRepository.findAllByUserId(userId);
        log.info("Found {} product reviews for user ID: {}", reviews.size(), userId);

        return reviews.stream()
                .map(reviewMapper::toProductReviewGetAllResponse)
                .toList();
    }

    /**
     * Calculates the average rating of all site reviews.
     *
     * @return the average rating as a Double
     */
    @Transactional(readOnly = true)
    public Double getAverageSiteRate() {
        Double averageRate = siteReviewRepository.getAverageRate();
        log.info("Calculated average site rate: {}", averageRate);
        return averageRate;
    }

    @Transactional(readOnly = true)
    public Double getAverageProductRate(Long productId) {
        Double averageRate = productReviewRepository.getAverageRate(productId);
        log.info("Calculated average product rate: {}", averageRate);
        return averageRate;
    }

    /**
     * Toggles the active status of a site review.
     *
     * @param id the ID of the review to activate/deactivate
     * @return true if the review is now active, false otherwise
     */
    @Transactional
    public boolean activateSiteReview(Long id) {
        log.info("Attempting to toggle activation status for site review ID: {}", id);
        var review = findSiteReviewOrThrow(id);

        if (review.isActive()) {
            review.setActive(false);
            log.info("Site Review ID: {} has been deactivated", id);
            return false;
        } else {
            review.setActive(true);
            log.info("Site Review ID: {} has been activated", id);
            return true;
        }
    }

    @Transactional
    public boolean activateProductReview(Long id) {
        log.info("Attempting to toggle activation status for product review ID: {}", id);
        var review = findProductReviewOrThrow(id);

        if (review.isActive()) {
            review.setActive(false);
            log.info("Product Review ID: {} has been deactivated", id);
            return false;
        } else {
            review.setActive(true);
            log.info("Product Review ID: {} has been activated", id);
            return true;
        }
    }


    /**
     * Updates an existing site review.
     *
     * @param request the updated review details
     * @param id      the ID of the review to update
     * @return the updated review response
     */
    @Transactional
    public SiteReviewUpdateResponse updateSiteReview(SiteReviewUpdateRequest request, Long id, Long userId) {
        log.info("Attempting to update site review with ID: {}", id);
        var review = findSiteReviewOrThrow(id);

        checkUserIdIsNotNull(userId);
        checkReviewIsActive(review, id);
        checkOwnerReview(review, userId, id);

        review.setRate(request.getRate());
        review.setDescription(request.getDescription());

        log.info("Successfully updated site review with ID: {}", id);
        return reviewMapper.toSiteReviewUpdateResponse(review);
    }

    @Transactional
    public ProductReviewUpdateResponse updateProductReview(ProductReviewUpdateRequest request, Long id, Long userId) {
        log.info("Attempting to update product review with ID: {}", id);
        var review = findProductReviewOrThrow(id);

        checkUserIdIsNotNull(userId);
        checkReviewIsActive(review, id);
        checkOwnerReview(review, userId, id);

        review.setRate(request.getRate());
        review.setDescription(request.getDescription());

        log.info("Successfully updated product review with ID: {}", id);
        return reviewMapper.toProductReviewUpdateResponse(review);
    }

    /**
     * Deletes a site review by its ID.
     *
     * @param id the ID of the review to delete
     */
    @Transactional
    public void deleteSiteReview(Long id, Long userId) {
        log.info("Attempting to delete site review with ID: {}", id);
        var review = findSiteReviewOrThrow(id);

        checkUserIdIsNotNull(userId);
        checkReviewIsActive(review, id);
        checkOwnerReview(review, userId, id);

        siteReviewRepository.delete(review);
        log.info("Successfully deleted site review with ID: {}", id);
    }

    @Transactional
    public void deleteProductReview(Long id, Long userId) {
        log.info("Attempting to delete product review with ID: {}", id);
        var review = findProductReviewOrThrow(id);

        checkUserIdIsNotNull(userId);
        checkReviewIsActive(review, id);
        checkOwnerReview(review, userId, id);

        productReviewRepository.delete(review);
        log.info("Successfully deleted product review with ID: {}", id);
    }

    /**
     * Helper method to find a review by ID or throw an exception if not found.
     *
     * @param id the ID of the review
     * @return the found SiteReview entity
     * @throws ReviewNotFoundException if the review does not exist
     */
    private SiteReview findSiteReviewOrThrow(Long id) {
        return siteReviewRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Failed to find site review with ID: {}", id);
                    return new ReviewNotFoundException("Site review not found!");
                });
    }

    private ProductReview findProductReviewOrThrow(Long id) {
        return productReviewRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Failed to find product review with ID: {}", id);
                    return new ReviewNotFoundException("Product review not found!");
                });
    }

    /**
     * Validates that the user ID is not null.
     *
     * @param id the user ID to check
     * @throws UnauthorizedException if the user ID is null
     */
    private void checkUserIdIsNotNull(Long id) {
        if (id == null) {
            log.error("Delete failed: User ID is null");
            throw new UnauthorizedException("Unauthorized user!");
        }
    }

    /**
     * Validates that a review is currently active.
     *
     * @param review the review entity to check
     * @param id     the ID of the review for logging purposes
     * @throws ReviewIsNotActiveException if the review is inactive
     */
    private void checkReviewIsActive(AbstractReview review, Long id) {
        if (!review.isActive()) {
            log.error("Review ID: {} is not active", id);
            throw new ReviewIsNotActiveException("Review is not active!");
        }
    }

    /**
     * Validates that the review belongs to the specified user.
     *
     * @param review   the review entity to check
     * @param userId   the ID of the user attempting the action
     * @param reviewId the ID of the review for logging purposes
     * @throws InvalidArgumentException if the user is not the owner of the review
     */
    private void checkOwnerReview(AbstractReview review, Long userId, Long reviewId) {
        if (!review.getUserId().equals(userId)) {
            log.error("Review ID: {} is not owned by user ID: {}", reviewId, userId);
            throw new InvalidArgumentException("It's not your review!");
        }
    }

}
