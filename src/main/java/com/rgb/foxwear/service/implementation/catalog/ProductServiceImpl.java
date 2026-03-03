package com.rgb.foxwear.service.implementation.catalog;

import com.rgb.foxwear.dto.request.catalog.CreateProductRequest;
import com.rgb.foxwear.dto.response.catalog.CreateCategoryResponse;
import com.rgb.foxwear.dto.response.catalog.CreateProductResponse;
import com.rgb.foxwear.entity.catalog.Product;
import com.rgb.foxwear.entity.catalog.WearCategory;
import com.rgb.foxwear.exception.InvalidArgumentException;
import com.rgb.foxwear.exception.WearCategoryNotFound;
import com.rgb.foxwear.repository.catalog.ProductRepository;
import com.rgb.foxwear.repository.catalog.WearCategoryRepository;
import com.rgb.foxwear.service.abstraction.catalog.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final WearCategoryRepository wearCategoryRepository;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public CreateProductResponse createProduct(CreateProductRequest request) {
        if (request.getDiscountPrice() != null && request.getDiscountPrice().compareTo(request.getOriginalPrice()) >= 0) {
            throw new InvalidArgumentException("Discount price cannot be greater than or equal to original price");
        }

        Product product = mapper.map(request, Product.class);
        product.setId(null); // ? to prevent incorrect id mapping

        product.setHasDiscount(request.getDiscountRate() != null && request.getDiscountRate() > 0);

        WearCategory category = wearCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new WearCategoryNotFound("Wear category not found with id " + request.getCategoryId()));

        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return getCreateProductResponse(savedProduct);
    }

    private CreateProductResponse getCreateProductResponse(Product product) {
        var response = mapper.map(product, CreateProductResponse.class);
        response.setCategory(
                mapper.map(product.getCategory(), CreateCategoryResponse.class)
        );

        if (response.getCategory().getParent() != null) {
            response.getCategory().setParent(
                    mapper.map(product.getCategory().getParent(), CreateCategoryResponse.class)
            );
        }

        return response;
    }
}
