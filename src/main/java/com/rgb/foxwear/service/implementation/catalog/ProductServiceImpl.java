package com.rgb.foxwear.service.implementation.catalog;

import com.rgb.foxwear.dto.request.catalog.ColorOptionCreateRequest;
import com.rgb.foxwear.dto.request.catalog.ProductCreateRequest;
import com.rgb.foxwear.dto.response.catalog.ColorOptionCreateResponse;
import com.rgb.foxwear.dto.response.catalog.ImageCreateResponse;
import com.rgb.foxwear.dto.response.catalog.ItemCreateResponse;
import com.rgb.foxwear.dto.response.catalog.ProductCreateResponse;
import com.rgb.foxwear.entity.catalog.*;
import com.rgb.foxwear.exception.ProductSizeNotFoundException;
import com.rgb.foxwear.exception.WearCategoryNotFound;
import com.rgb.foxwear.repository.catalog.ProductRepository;
import com.rgb.foxwear.repository.catalog.ProductSizeRepository;
import com.rgb.foxwear.repository.catalog.WearCategoryRepository;
import com.rgb.foxwear.service.abstraction.catalog.ProductService;
import com.rgb.foxwear.util.CodeGenerator;
import com.rgb.foxwear.util.StringHelper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final WearCategoryRepository categoryRepository;
    private final ProductSizeRepository productSizeRepository;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public ProductCreateResponse createProduct(ProductCreateRequest request) {
        // 1. Validate and retrieve the category
        WearCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new WearCategoryNotFound("Category not found"));

        // 2. Map request to Product entity and set basic details
        Product product = mapper.map(request, Product.class);
        product.setId(null);
        product.setCategory(category);
        product.setTitle(StringHelper.capitalize(request.getTitle()));
        product.setSlug(StringHelper.generateSlug(request.getTitle()));

        // 3. Process color options, including images and individual product items (sizes/stock)
        List<ColorOption> colorOptions = request.getColors().stream()
                .map(color -> {
                    ColorOption colorOption = mapper.map(color, ColorOption.class);

                    colorOption.setId(null);
                    colorOption.setProduct(product);
                    colorOption.setColorName(StringHelper.capitalize(colorOption.getColorName()));
                    colorOption.setColorCode(colorOption.getColorCode().trim());

                    // Map nested images and items using helper methods
                    List<ColorOptionImage> images = mapImages(color, colorOption);
                    List<ProductItem> items = mapItems(color, colorOption);

                    colorOption.setImages(images);
                    colorOption.setItems(items);

                    return colorOption;
                })
                .toList();

        product.setColors(colorOptions);

        // 4. Persist the complete product hierarchy and return response
        Product savedProduct = productRepository.save(product);

        return getResponse(savedProduct);
    }

    /**
     * Maps image requests to {@link ColorOptionImage} entities and associates them with a {@link ColorOption}.
     */
    private List<ColorOptionImage> mapImages(ColorOptionCreateRequest color, ColorOption colorOption) {
        return color.getImages().stream()
                .map(image -> {
                    ColorOptionImage colorOptionImage = mapper.map(image, ColorOptionImage.class);
                    colorOptionImage.setId(null);
                    colorOptionImage.setColorOption(colorOption);

                    return colorOptionImage;
                })
                .toList();
    }

    /**
     * Maps item requests to {@link ProductItem} entities, looks up the corresponding {@link ProductSize},
     * and generates unique SKUs.
     */
    private List<ProductItem> mapItems(ColorOptionCreateRequest color, ColorOption colorOption) {
        return color.getItems().stream()
                .map(item -> {
                    ProductSize productSize = productSizeRepository.findById(item.getSizeId())
                            .orElseThrow(() -> new ProductSizeNotFoundException("Product size not found"));

                    return ProductItem.builder()
                            .colorOption(colorOption)
                            .productSize(productSize)
                            .sku(CodeGenerator.generateSku())
                            .stockQuantity(item.getStockQuantity())
                            .stockRemaining(item.getStockQuantity()) // Initialize remaining stock
                            .isDeleted(false)
                            .build();
                })
                .toList();
    }

    /**
     * Transforms the saved {@link Product} entity and its nested hierarchy into a {@link ProductCreateResponse} DTO.
     */
    private ProductCreateResponse getResponse(Product product) {
        ProductCreateResponse response = mapper.map(product, ProductCreateResponse.class);
        response.setCategoryName(product.getCategory().getName());

        List<ColorOptionCreateResponse> colors = product.getColors().stream()
                .map(color -> {
                    ColorOptionCreateResponse colorResponse = mapper.map(color, ColorOptionCreateResponse.class);

                    List<ImageCreateResponse> images = color.getImages().stream()
                            .map(image -> {
                                ImageCreateResponse imageResponse = mapper.map(image, ImageCreateResponse.class);
                                imageResponse.setIsMain(image.isMain());

                                return imageResponse;
                            })
                            .toList();

                    List<ItemCreateResponse> items = color.getItems().stream()
                            .map(item -> mapper.map(item, ItemCreateResponse.class))
                            .toList();

                    colorResponse.setImages(images);
                    colorResponse.setItems(items);

                    return colorResponse;
                })
                .toList();

        response.setColors(colors);

        return response;
    }
}
