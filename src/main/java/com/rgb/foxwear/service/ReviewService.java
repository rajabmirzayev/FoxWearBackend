package com.rgb.foxwear.service;

import com.rgb.foxwear.dto.response.auth.UserGetReviewResponse;
import com.rgb.foxwear.dto.response.interaction.ReviewGetAllResponse;
import com.rgb.foxwear.repository.interaction.SiteReviewRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final SiteReviewRepository siteReviewRepository;
    private final ModelMapper modelMapper;

    public List<ReviewGetAllResponse> getFirst10Reviews() {
        var response = siteReviewRepository.findAllFirst10(PageRequest.of(0, 10));

        return response.stream()
                .map(r -> {
                    var review = modelMapper.map(r, ReviewGetAllResponse.class);
                    review.setUser(
                            modelMapper.map(r.getUser(), UserGetReviewResponse.class)
                    );

                    return review;
                })
                .toList();
    }
}
