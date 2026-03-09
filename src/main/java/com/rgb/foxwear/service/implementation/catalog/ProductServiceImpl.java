package com.rgb.foxwear.service.implementation.catalog;

import com.rgb.foxwear.dto.request.catalog.CategoryCreateRequest;
import com.rgb.foxwear.dto.request.catalog.ColorOptionCreateRequest;
import com.rgb.foxwear.dto.request.catalog.ProductCreateRequest;
import com.rgb.foxwear.dto.request.catalog.SizeCreateRequest;
import com.rgb.foxwear.dto.response.catalog.*;
import com.rgb.foxwear.entity.catalog.*;
import com.rgb.foxwear.exception.*;
import com.rgb.foxwear.repository.catalog.*;
import com.rgb.foxwear.service.abstraction.catalog.ProductService;
import com.rgb.foxwear.util.CodeGenerator;
import com.rgb.foxwear.util.StringHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final WearCategoryRepository categoryRepository;
    private final ProductSizeRepository productSizeRepository;
    private final ColorOptionRepository colorOptionRepository;
    private final ProductItemRepository productItemRepository;
    private final ModelMapper mapper;

    /**
     * Creates a new product including its category association and color options.
     */
    @Override
    @Transactional
    public ProductCreateResponse createProduct(ProductCreateRequest request) {
        log.info("Creating new product with title: {}", request.getTitle());
        checkPrices(request);

        // 1. Validate and retrieve the category
        WearCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> {
                    log.error("Category not found with ID: {}", request.getCategoryId());
                    return new WearCategoryNotFoundException("Category not found");
                });

        // 2. Map request to Product entity and set basic details
        Product product = mapper.map(request, Product.class);
        product.setId(null);
        product.setCategory(category);
        product.setTitle(StringHelper.capitalize(request.getTitle()));
        product.setSlug(StringHelper.generateSlug(request.getTitle()));

        // 3. Process color options, including images and individual product items (sizes/stock)
        List<ColorOption> colorOptions = request.getColors().stream()
                .map(color -> mapToColorOption(color, product))
                .toList();

        product.setColors(colorOptions);

        // 4. Persist the complete product hierarchy and return response
        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with ID: {}", savedProduct.getId());

        return getProductResponse(savedProduct);
    }

    /**
     * Adds a new color option to an existing product.
     */
    @Override
    @Transactional
    public ColorOptionCreateResponse addColorToProduct(Long productId, ColorOptionCreateRequest request) {
        log.info("Adding color {} to product ID: {}", request.getColorName(), productId);
        Product product = findProductOrThrow(productId);

        ColorOption colorOption = mapToColorOption(request, product);

        // If the product is used within this method (for future)
        product.getColors().add(colorOption);

        var savedColor = colorOptionRepository.save(colorOption);
        log.info("Color option added successfully with ID: {}", savedColor.getId());

        return getColorResponse(savedColor);
    }

    /**
     * Creates a new product category, optionally associating it with a parent category.
     */
    @Override
    @Transactional
    public CategoryCreateResponse createCategory(CategoryCreateRequest request) {
        log.info("Creating new category with name: {}", request.getName());

        WearCategory category = mapper.map(request, WearCategory.class);
        category.setId(null);
        category.setName(StringHelper.capitalize(request.getName()));

        if (request.getParentId() != null) {
            WearCategory parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> {
                        log.error("Parent category not found with ID: {}", request.getParentId());
                        return new WearCategoryNotFoundException("Parent category not found");
                    });
            category.setParent(parent);
        }

        WearCategory savedCategory = categoryRepository.save(category);
        log.info("Category created successfully with ID: {}", savedCategory.getId());

        return mapper.map(savedCategory, CategoryCreateResponse.class);
    }

    /**
     * Creates a new product size definition.
     */
    @Override
    @Transactional
    public SizeCreateResponse createSize(SizeCreateRequest request) {
        log.info("Creating new size with value: {}", request.getSizeValue());

        if(productSizeRepository.findBySizeValue(request.getSizeValue()).isPresent()) {
            log.warn("Size with value {} already exists", request.getSizeValue());
            throw new SizeAlreadyExistsException("Size already exists");
        }

        ProductSize size = mapper.map(request, ProductSize.class);
        size.setId(null);

        var savedSize = productSizeRepository.save(size);
        log.info("Size created successfully with ID: {}", savedSize.getId());

        return mapper.map(savedSize, SizeCreateResponse.class);
    }

    /**
     * Updates the active status of a product.
     */
    @Override
    @Transactional
    public void updateProductActivity(Long id, boolean isActive) {
        Product product = findProductOrThrow(id);

        if (product.isActive() != isActive) {
            product.setActive(isActive);
            log.info("Product ID: {} activity updated to: {}", id, isActive);
        }
    }

    /**
     * Marks a product as deleted without removing it from the database.
     */
    @Override
    @Transactional
    public void softDeleteProduct(Long id) {
        Product product = findProductOrThrow(id);

        if (!product.isDeleted()) {
            product.setDeleted(true);
            log.info("Product soft deleted successfully with ID: {}", id);
        }
    }

    /**
     * Permanently deletes a product from the database.
     */
    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = findProductOrThrow(id);

        productRepository.delete(product);
        log.info("Product hard deleted successfully with ID: {}", id);
    }

    /**
     * Marks a specific product item as deleted.
     */
    @Override
    @Transactional
    public void softDeleteProductItem(Long id) {
        ProductItem item = findProductItemOrThrow(id);

        if (!item.isDeleted()) {
            item.setDeleted(true);
            log.info("Product item soft deleted successfully with ID: {}", id);
        }
    }

    /**
     * Permanently deletes a specific product item from the database.
     */
    @Override
    @Transactional
    public void deleteProductItem(Long id) {
        ProductItem item = findProductItemOrThrow(id);

        productItemRepository.delete(item);
        log.info("Product item hard deleted successfully with ID: {}", id);
    }

    /**
     * Updates the stock quantity for a specific product item.
     */
    @Override
    @Transactional
    public ItemUpdateResponse updateStock(Long itemId, Integer count) {
        log.info("Updating stock for item ID: {} to count: {}", itemId, count);

        if (count < 0) {
            throw new InvalidArgumentException("Stock quantity cannot be negative!");
        }

        ProductItem productItem = findProductItemOrThrow(itemId);

        if (productItem.isDeleted()) {
            log.warn("Attempted to update stock for deleted item ID: {}", itemId);
            throw new ProductIsDeletedException("Product item is deleted!");
        }

        productItem.setStockQuantity(count);
        productItem.setStockRemaining(count);

        log.info("Stock updated successfully for item ID: {}", productItem.getId());

        return mapper.map(productItem, ItemUpdateResponse.class);
    }

    /**
     * Helper method to find a {@link Product} by ID or throw a {@link ProductNotFoundException}.
     */
    private Product findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product not found with ID: {}", id);
                    return new ProductNotFoundException("Product not found!");
                });
    }

    /**
     * Helper method to find a {@link ProductItem} by ID or throw a {@link ProductNotFoundException}.
     */
    private ProductItem findProductItemOrThrow(Long id) {
        return productItemRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product item not found with ID: {}", id);
                    return new ProductNotFoundException("Product item not found!");
                });
    }

    /**
     * Maps a {@link ColorOptionCreateRequest} to a {@link ColorOption} entity, including nested images and items.
     */
    private ColorOption mapToColorOption(ColorOptionCreateRequest colorOptionCreateRequest, Product product) {
        ColorOption colorOption = mapper.map(colorOptionCreateRequest, ColorOption.class);

        colorOption.setId(null);
        colorOption.setProduct(product);
        colorOption.setColorName(StringHelper.capitalize(colorOption.getColorName()));
        colorOption.setColorCode(colorOption.getColorCode().trim());

        // Map nested images and items using helper methods
        List<ColorOptionImage> images = mapImages(colorOptionCreateRequest, colorOption);
        List<ProductItem> items = mapItems(colorOptionCreateRequest, colorOption);

        colorOption.setImages(images);
        colorOption.setItems(items);

        return colorOption;
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
                    colorOptionImage.setMain(image.getIsMain());

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
                            .orElseThrow(() -> {
                                log.error("Product size not found with ID: {}", item.getSizeId());
                                return new ProductSizeNotFoundException("Product size not found");
                            });

                    return ProductItem.builder()
                            .colorOption(colorOption)
                            .productSize(productSize)
                            .sku(CodeGenerator.generateSku())
                            .stockQuantity(item.getStockQuantity())
                            .stockRemaining(item.getStockQuantity())
                            .isDeleted(false)
                            .build();
                })
                .toList();
    }

    /**
     * Transforms the saved {@link Product} entity and its nested hierarchy into a {@link ProductCreateResponse} DTO.
     */
    private ProductCreateResponse getProductResponse(Product product) {
        ProductCreateResponse response = mapper.map(product, ProductCreateResponse.class);
        response.setCategoryName(product.getCategory().getName());

        List<ColorOptionCreateResponse> colors = product.getColors().stream()
                .map(this::getColorResponse)
                .toList();

        response.setColors(colors);

        return response;
    }

    /**
     * Transforms a {@link ColorOption} entity and its nested images and items into a {@link ColorOptionCreateResponse} DTO.
     */
    private ColorOptionCreateResponse getColorResponse(ColorOption color) {
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
    }

    /**
     * Validates that the price and discount information in the {@link ProductCreateRequest} are logically consistent.
     */
    private void checkPrices(ProductCreateRequest request) {
        if (request.getOriginalPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new InvalidArgumentException("Original price must be greater than zero");
        }

        if (request.getDiscountPrice() != null &&
                request.getOriginalPrice().compareTo(request.getDiscountPrice()) <= 0) {
            throw new InvalidArgumentException("Original price must be greater than discount price");
        }

        if (request.getDiscountRate() != null && (request.getDiscountRate() < 0 || request.getDiscountRate() > 100)) {
            throw new InvalidArgumentException("Discount rate must be between 0 and 100");
        }
    }
}
