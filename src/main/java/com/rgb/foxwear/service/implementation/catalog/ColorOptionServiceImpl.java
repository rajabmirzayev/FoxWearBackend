package com.rgb.foxwear.service.implementation.catalog;

import com.rgb.foxwear.dto.request.catalog.CreateColorOptionRequest;
import com.rgb.foxwear.dto.response.catalog.CreateColorOptionResponse;
import com.rgb.foxwear.dto.response.catalog.GetAllProductResponse;
import com.rgb.foxwear.entity.catalog.ColorOption;
import com.rgb.foxwear.entity.catalog.Product;
import com.rgb.foxwear.repository.catalog.ColorOptionRepository;
import com.rgb.foxwear.repository.catalog.ProductRepository;
import com.rgb.foxwear.service.abstraction.catalog.ColorOptionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.nio.file.ProviderNotFoundException;

@Service
@RequiredArgsConstructor
public class ColorOptionServiceImpl implements ColorOptionService {
    private final ColorOptionRepository colorOptionRepository;
    private final ProductRepository productRepository;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public CreateColorOptionResponse createColorOption(CreateColorOptionRequest request) {
        ColorOption colorOption = mapper.map(request, ColorOption.class);
        colorOption.setId(null); // ? to prevent incorrect id mapping

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProviderNotFoundException("Product not found with id " + request.getProductId()));

        colorOption.setProduct(product);

        ColorOption savedColorOption = colorOptionRepository.save(colorOption);

        return getCreateColorOptionResponse(savedColorOption);
    }

    private CreateColorOptionResponse getCreateColorOptionResponse(ColorOption colorOption) {
        var response = mapper.map(colorOption, CreateColorOptionResponse.class);

        response.setProduct(mapper.map(colorOption.getProduct(), GetAllProductResponse.class));

        return response;
    }
}
