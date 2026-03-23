package com.foxwear.dynamicdataservice.service;

import com.foxwear.dynamicdataservice.dto.request.BannerRequest;
import com.foxwear.dynamicdataservice.dto.response.BannerResponse;
import com.foxwear.dynamicdataservice.entity.Banner;
import com.foxwear.dynamicdataservice.exception.BannerAlreadyExistsException;
import com.foxwear.dynamicdataservice.exception.BannerIsNotActiveException;
import com.foxwear.dynamicdataservice.exception.BannerNotFoundException;
import com.foxwear.dynamicdataservice.mapper.BannerMapper;
import com.foxwear.dynamicdataservice.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing dynamic content such as Banners.
 * Handles business logic, validation, and persistence mapping.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DynamicService {
    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;

    /**
     * Creates a new banner from the provided request data.
     *
     * @param request the banner creation details
     * @return the created banner details as a response DTO
     */
    @Transactional
    public BannerResponse createBanner(BannerRequest request) {
        if (bannerRepository.existsByPlacement(request.getPlacement())) {
            log.error("Banner with placement {} already exists", request.getPlacement());
            throw new BannerAlreadyExistsException("Banner with placement " + request.getPlacement() + " already exists.");
        }

        log.info("Creating new banner with title: {} and placement: {}", request.getTitle(), request.getPlacement());

        Banner banner = bannerMapper.toEntity(request);
        banner = bannerRepository.save(banner);

        log.info("Banner created successfully with id: {}", banner.getId());
        return bannerMapper.toResponse(banner);
    }

    /**
     * Retrieves an active banner by its placement identifier.
     *
     * @param placement the unique placement string
     * @return the active banner details
     */
    @Transactional(readOnly = true)
    public BannerResponse getBanner(String placement) {
        log.info("Fetching banner with placement: {}", placement);

        Banner banner = bannerRepository.findByPlacementAndActive(placement, true)
                .orElseThrow(() -> new BannerNotFoundException("Banner with placement " + placement + " not found"));

        if (!banner.isActive()) {
            log.error("Banner with placement {} is not active", placement);
            throw new BannerIsNotActiveException("Banner with placement " + placement + " is not active");
        }

        log.info("Successfully retrieved active banner for placement: {}", placement);
        return bannerMapper.toResponse(banner);
    }

    /**
     * Updates an existing banner's information.
     *
     * @param request the updated banner data
     * @param id      the ID of the banner to update
     * @return the updated banner details
     */
    @Transactional
    public BannerResponse updateBanner(BannerRequest request, Long id) {
        log.info("Updating banner with placement: {}", request.getPlacement());
        if (bannerRepository.existsByPlacementAndIdNot(request.getPlacement(), id)) {
            log.error("Banner with placement: {} already exists", request.getPlacement());
            throw new BannerAlreadyExistsException("Banner with placement " + request.getPlacement() + " already exists");
        }

        Banner banner = findBannerOrThrow(request.getPlacement());
        banner.setImageUrl(request.getImageUrl());
        banner.setMobileImageUrl(request.getMobileImageUrl());
        banner.setTitle(request.getTitle());
        banner.setSubtitle(request.getSubtitle());
        banner.setButtonText(request.getButtonText());
        banner.setButtonLink(request.getButtonLink());
        banner.setSortOrder(request.getSortOrder());
        banner.setActive(request.isActive());

        log.info("Banner updated successfully with id: {}", banner.getId());
        return bannerMapper.toResponse(banner);
    }

    /**
     * Deletes a banner from the system by its ID.
     *
     * @param id the ID of the banner to delete
     */
    @Transactional
    public void deleteBanner(Long id) {
        log.info("Deleting banner with id: {}", id);
        Banner banner = findBannerOrThrow(id);

        bannerRepository.delete(banner);
        log.info("Banner deleted successfully with id: {}", id);
    }

    /**
     * Helper method to find a banner by placement or throw an exception.
     *
     * @param placement the placement string
     * @return the found Banner entity
     */
    private Banner findBannerOrThrow(String placement) {
        return bannerRepository.findByPlacement(placement)
                .orElseThrow(() -> {
                    log.error("Banner not found with placement {}", placement);
                    return new BannerNotFoundException("Banner not found!");
                });
    }

    /**
     * Helper method to find a banner by ID or throw an exception.
     *
     * @param id the banner ID
     * @return the found Banner entity
     */
    private Banner findBannerOrThrow(Long id) {
        return bannerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Banner not found with id {}", id);
                    return new BannerNotFoundException("Banner not found!");
                });
    }

}
