package com.foxwear.orderservice.service;

import com.foxwear.orderservice.dto.request.CouponCreateRequest;
import com.foxwear.orderservice.dto.response.CouponCreateResponse;
import com.foxwear.orderservice.dto.response.CouponGetResponse;
import com.foxwear.orderservice.entity.Coupon;
import com.foxwear.orderservice.exception.CouponAlreadyExistsException;
import com.foxwear.orderservice.exception.CouponNotFoundException;
import com.foxwear.orderservice.mapper.CouponMapper;
import com.foxwear.orderservice.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    /**
     * Creates a new coupon if the code doesn't already exist.
     *
     * @param request the coupon creation details
     * @return the created coupon response
     */
    @Transactional
    public CouponCreateResponse createCoupon(CouponCreateRequest request) {
        if (couponRepository.existsByCode(request.getCode())) {
            log.error("Coupon already exists with code {}", request.getCode());
            throw new CouponAlreadyExistsException("Coupon with code " + request.getCode() + " already exists");
        }

        log.info("Creating new coupon with code: {}", request.getCode());
        Coupon coupon = couponMapper.toEntity(request);
        var savedCoupon = couponRepository.save(coupon);

        return couponMapper.toCreateResponse(savedCoupon);
    }

    /**
     * Retrieves a coupon by its unique identifier.
     *
     * @param id the coupon ID
     * @return the coupon details
     */
    @Transactional(readOnly = true)
    public CouponGetResponse getCouponById(Long id) {
        log.debug("Fetching coupon by id: {}", id);
        Coupon coupon = findCouponOrThrow(id);

        return couponMapper.toGetResponse(coupon);
    }

    /**
     * Retrieves a coupon by its unique code.
     *
     * @param code the coupon code
     * @return the coupon details
     */
    @Transactional(readOnly = true)
    public CouponGetResponse getCouponByCode(String code) {
        log.debug("Fetching coupon by code: {}", code);
        Coupon coupon = findCouponOrThrow(code);

        return couponMapper.toGetResponse(coupon);
    }

    /**
     * Increments the usage count of a specific coupon.
     *
     * @param id the coupon ID
     */
    @Transactional
    public void increaseUsedCount(Long id) {
        log.info("Increasing usage count for coupon id: {}", id);
        Coupon coupon = findCouponOrThrow(id);
        coupon.setUsedCount(coupon.getUsedCount() + 1);
    }

    /**
     * Toggles the active status of a coupon.
     *
     * @param couponId the coupon ID
     * @return the new status of the coupon (true for active, false for inactive)
     */
    @Transactional
    public boolean toggleActivate(Long couponId) {
        Coupon coupon = findCouponOrThrow(couponId);

        if (coupon.isActive()) {
            coupon.setActive(false);
            log.info("Coupon has been deactivated with code {}", coupon.getCode());
            return false;
        } else {
            coupon.setActive(true);
            log.info("Coupon has been activated with code {}", coupon.getCode());
            return true;
        }
    }

    private Coupon findCouponOrThrow(Long id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Coupon not found with id {}", id);
                    return new CouponNotFoundException("Coupon not found");
                });
    }

    public Coupon findCouponOrThrow(String code) {
        return couponRepository.findByCode(code)
                .orElseThrow(() -> {
                    log.error("Coupon not found with code {}", code);
                    return new CouponNotFoundException("Coupon not found");
                });
    }

}
