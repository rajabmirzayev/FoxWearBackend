package com.rgb.foxwear.service;

import com.rgb.foxwear.dto.request.dynamic.BannerRequest;
import com.rgb.foxwear.dto.response.dynamic.BannerResponse;
import com.rgb.foxwear.entity.dynamic.Banner;
import com.rgb.foxwear.exception.BannerAlreadyExistsException;
import com.rgb.foxwear.exception.BannerIsNotActiveException;
import com.rgb.foxwear.exception.BannerNotFoundException;
import com.rgb.foxwear.repository.dynamic.BannerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DynamicService {
    private final BannerRepository bannerRepository;
    private final ModelMapper mapper;

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
            throw new BannerAlreadyExistsException("Banner with placement " + request.getPlacement() + " already exists");
        }

        log.info("Creating new banner with title: {}", request.getTitle());
        Banner banner = mapper.map(request, Banner.class);
        banner = bannerRepository.save(banner);

        log.info("Banner created successfully with id: {}", banner.getId());
        return mapper.map(banner, BannerResponse.class);
    }

    @Transactional(readOnly = true)
    public BannerResponse getBanner(String placement) {
        log.info("Fetching banner with placement: {}", placement);

        Banner banner = bannerRepository.findByPlacementAndActive(placement, true)
                .orElseThrow(() -> new BannerNotFoundException("Banner with placement " + placement + " not found"));

        if (!banner.isActive()) {
            log.error("Banner with placement {} is not active", placement);
            throw new BannerIsNotActiveException("Banner with placement " + placement + " is not active");
        }

        return mapper.map(banner, BannerResponse.class);
    }

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
        return mapper.map(banner, BannerResponse.class);
    }

    @Transactional
    public void deleteBanner(Long id) {
        log.info("Deleting banner with id: {}", id);
        Banner banner = findBannerOrThrow(id);

        bannerRepository.delete(banner);
        log.info("Banner deleted successfully with id: {}", id);
    }

    private Banner findBannerOrThrow(String placement) {
        return bannerRepository.findByPlacement(placement)
                .orElseThrow(() -> {
                    log.error("Banner not found with placement {}", placement);
                    return new BannerNotFoundException("Banner not found!");
                });
    }

    private Banner findBannerOrThrow(Long id) {
        return bannerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Banner not found with id {}", id);
                    return new BannerNotFoundException("Banner not found!");
                });
    }

}
