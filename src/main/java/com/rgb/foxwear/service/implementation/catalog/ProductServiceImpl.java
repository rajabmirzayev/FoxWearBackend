package com.rgb.foxwear.service.implementation.catalog;

import com.rgb.foxwear.dto.request.catalog.*;
import com.rgb.foxwear.dto.response.catalog.*;
import com.rgb.foxwear.entity.catalog.*;
import com.rgb.foxwear.exception.*;
import com.rgb.foxwear.repository.catalog.*;
import com.rgb.foxwear.repository.catalog.specification.ProductSpecification;
import com.rgb.foxwear.service.abstraction.catalog.ProductService;
import com.rgb.foxwear.util.CodeGenerator;
import com.rgb.foxwear.util.StringHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j  // add logs and comments where it needs it
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
        WearCategory category = findCategoryOrThrow(request.getCategoryId());

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

        return getProductCreateResponse(savedProduct);
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

        return getColorCreateResponse(savedColor);
    }

    /**
     * Creates a new product category, optionally associating it with a parent category.
     */
    @Override
    @Transactional
    public CategoryCreateResponse createCategory(CategoryCreateRequest request) {
        log.info("Creating new category with name: {}", request.getName());
        checkCategory(request);

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

        if (productSizeRepository.findBySizeValue(request.getSizeValue()).isPresent()) {
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
     * Retrieves a paginated list of products filtered by admin-defined criteria.
     */
    @Override
    @Transactional
    public Page<@NonNull ProductGetAllResponse> getAllProductWithAdminFilter(ProductAdminFilterRequest filter) {
        log.info("Fetching products with admin filter: {}", filter);
        Pageable pageable = PageRequest.of(
                filter.getPage(),
                filter.getSize(),
                Sort.by(filter.getDirection(), filter.getSortBy())
        );

        var products = productRepository.findAll(buildSpecification(filter), pageable);

        String searchKeyword = filter.getKeyword() == null ? null : filter.getKeyword().toLowerCase();
        String filterColor = filter.getColor() == null ? null : filter.getColor().toLowerCase();
        String filterSize = filter.getProductSize() == null ? null : filter.getProductSize().toLowerCase();

        return products.map(product -> {
            ProductGetAllResponse productResponse = mapper.map(product, ProductGetAllResponse.class);
            productResponse.setCategory(mapper.map(product.getCategory(), CategoryGetAllResponse.class));
            productResponse.setColors(product.getColors().stream()
                    .map(this::getColorOptionGetAllResponse)
                    .collect(Collectors.toCollection(ArrayList::new)));

            if (productResponse.getColors() != null) {
                var matchingColor = productResponse.getColors().stream()
                        .filter(c -> isMatch(c, searchKeyword, filterColor, filterSize))
                        .findFirst();

                matchingColor.ifPresent(color -> {
                    productResponse.getColors().remove(color);
                    productResponse.getColors().addFirst(color);
                });
            }

            return productResponse;
        });
    }

    /**
     * Updates an existing product's details and its color options.
     */
    @Override
    @Transactional
    public ProductUpdateResponse updateProduct(ProductUpdateRequest request, Long id) {
        log.info("Updating product ID: {} with title: {}", id, request.getTitle());
        checkPrices(request);

        Product product = findProductOrThrow(id);
        WearCategory category = findCategoryOrThrow(request.getCategoryId());

        updateProductFields(product, request, category);

        List<ColorOption> colorOptions = request.getColors().stream()
                .map(color -> mapToColorOption(color, product))
                .toList();

        product.getColors().clear();
        product.getColors().addAll(colorOptions);

        log.info("Product updated successfully with ID: {}", product.getId());

        return getProductUpdateResponse(product);
    }

    /**
     * Updates the activation status of an existing product.
     */
    @Override
    @Transactional
    public void updateProductActivity(Long id, boolean isActive) {
        log.info("Updating activity status for product ID: {} to {}", id, isActive);
        Product product = findProductOrThrow(id);

        if (product.isActive() != isActive) {
            product.setActive(isActive);
            log.info("Product ID: {} activity updated successfully", id);
        }
    }

    /**
     * Marks a product as deleted (soft delete) to preserve referential integrity.
     */
    @Override
    @Transactional
    public void softDeleteProduct(Long id) {
        log.info("Soft deleting product ID: {}", id);
        Product product = findProductOrThrow(id);

        if (!product.isDeleted()) {
            product.setDeleted(true);
            log.info("Product soft deleted successfully with ID: {}", id);
        }
    }

    /**
     * Permanently removes a product and its associated options from the database.
     */
    @Override
    @Transactional
    public void deleteProduct(Long id) {
        log.info("Hard deleting product ID: {}", id);
        Product product = findProductOrThrow(id);

        productRepository.delete(product);
        log.info("Product hard deleted successfully with ID: {}", id);
    }

    /**
     * Marks a specific product item (size/sku variant) as deleted.
     */
    @Override
    @Transactional
    public void softDeleteProductItem(Long id) {
        log.info("Soft deleting product item ID: {}", id);
        ProductItem item = findProductItemOrThrow(id);

        if (!item.isDeleted()) {
            item.setDeleted(true);
            log.info("Product item soft deleted successfully with ID: {}", id);
        }
    }

    /**
     * Permanently removes a specific product item variant from the database.
     */
    @Override
    @Transactional
    public void deleteProductItem(Long id) {
        log.info("Hard deleting product item ID: {}", id);
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
     * Helper method to find a {@link WearCategory} by ID or throw a {@link WearCategoryNotFoundException}.
     */
    private WearCategory findCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Category not found with ID: {}", id);
                    return new WearCategoryNotFoundException("Category not found");
                });
    }

    /**
     * Maps a {@link ColorOptionCreateRequest} to a {@link ColorOption} entity, including nested images and items.
     */
    private ColorOption mapToColorOption(ColorOptionDTO colorOptionDTO, Product product) {
        ColorOption colorOption = mapper.map(colorOptionDTO, ColorOption.class);

        colorOption.setId(null);
        colorOption.setProduct(product);
        colorOption.setColorName(StringHelper.capitalize(colorOption.getColorName()));
        colorOption.setColorCode(colorOption.getColorCode().trim());

        // Map nested images and items using helper methods
        List<ColorOptionImage> images = mapImages(colorOptionDTO, colorOption);
        List<ProductItem> items = mapItems(colorOptionDTO, colorOption);

        colorOption.setImages(images);
        colorOption.setItems(items);

        return colorOption;
    }

    /**
     * Maps image requests to {@link ColorOptionImage} entities and associates them with a {@link ColorOption}.
     */
    private List<ColorOptionImage> mapImages(ColorOptionDTO color, ColorOption colorOption) {
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
    private List<ProductItem> mapItems(ColorOptionDTO color, ColorOption colorOption) {
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
    private ProductCreateResponse getProductCreateResponse(Product product) {
        ProductCreateResponse response = mapper.map(product, ProductCreateResponse.class);
        response.setCategoryName(product.getCategory().getName());

        List<ColorOptionCreateResponse> colors = product.getColors().stream()
                .map(this::getColorCreateResponse)
                .toList();

        response.setColors(colors);

        return response;
    }

    /**
     * Transforms the updated {@link Product} entity and its nested hierarchy into a {@link ProductUpdateResponse} DTO.
     */
    private ProductUpdateResponse getProductUpdateResponse(Product product) {
        ProductUpdateResponse response = mapper.map(product, ProductUpdateResponse.class);
        response.setCategoryName(product.getCategory().getName());

        List<ColorOptionUpdateResponse> colors = product.getColors().stream()
                .map(this::getColorUpdateResponse)
                .toList();

        response.setColors(colors);

        return response;
    }

    /**
     * Transforms a {@link ColorOption} entity and its nested images and items into a {@link ColorOptionCreateResponse} DTO.
     */
    private ColorOptionCreateResponse getColorCreateResponse(ColorOption color) {
        ColorOptionCreateResponse colorResponse = mapper.map(color, ColorOptionCreateResponse.class);

        List<ImageCreateResponse> images = color.getImages().stream()
                .map(image -> {
                    ImageCreateResponse imageResponse = mapper.map(image, ImageCreateResponse.class);
                    imageResponse.setMain(image.isMain());

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
     * Transforms a {@link ColorOption} entity and its nested images and items into a {@link ColorOptionUpdateResponse} DTO.
     */
    private ColorOptionUpdateResponse getColorUpdateResponse(ColorOption color) {
        ColorOptionUpdateResponse colorResponse = mapper.map(color, ColorOptionUpdateResponse.class);

        List<ImageUpdateResponse> images = color.getImages().stream()
                .map(image -> {
                    ImageUpdateResponse imageResponse = mapper.map(image, ImageUpdateResponse.class);
                    imageResponse.setMain(image.isMain());

                    return imageResponse;
                })
                .toList();

        List<ItemUpdateResponse> items = color.getItems().stream()
                .map(item -> mapper.map(item, ItemUpdateResponse.class))
                .toList();

        colorResponse.setImages(images);
        colorResponse.setItems(items);

        return colorResponse;
    }

    private ColorOptionGetAllResponse getColorOptionGetAllResponse(ColorOption color) {
        ColorOptionGetAllResponse colorResponse = mapper.map(color, ColorOptionGetAllResponse.class);
        var images = colorResponse.getImages().stream()
                .map(image -> mapper.map(image, ColorOptionImageGetAllResponse.class))
                .toList();

        var items = colorResponse.getItems().stream()
                .map(item -> mapper.map(item, ItemGetAllResponse.class))
                .toList();

        colorResponse.setImages(images);
        colorResponse.setItems(items);

        return colorResponse;
    }

    /**
     * Updates the basic fields of a {@link Product} entity from a {@link ProductUpdateRequest}.
     */
    private void updateProductFields(Product product, ProductUpdateRequest request, WearCategory category) {
        product.setCategory(category);
        product.setTitle(StringHelper.capitalize(request.getTitle()));
        product.setSlug(StringHelper.generateSlug(request.getTitle()));
        product.setOriginalPrice(request.getOriginalPrice());
        product.setDiscountPrice(request.getDiscountPrice());
        product.setDiscountRate(request.getDiscountRate());
        product.setGender(request.getGender());
        product.setDescription(request.getDescription());
    }

    /**
     * Validates that the price and discount information in the {@link ProductCreateRequest} are logically consistent.
     */
    private void checkPrices(ProductCreateRequest request) {
        validatePrices(request.getOriginalPrice(), request.getDiscountPrice(), request.getDiscountRate());
    }

    /**
     * Validates that the price and discount information in the {@link ProductUpdateRequest} are logically consistent.
     */
    private void checkPrices(ProductUpdateRequest request) {
        validatePrices(request.getOriginalPrice(), request.getDiscountPrice(), request.getDiscountRate());
    }

    /**
     * Validates that the price and discount information are logically consistent.
     *
     * @param originalPrice the original price of the product
     * @param discountPrice the discounted price of the product
     * @param discountRate  the percentage of the discount
     */
    private void validatePrices(BigDecimal originalPrice, BigDecimal discountPrice, Integer discountRate) {
        if (originalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidArgumentException("Original price must be greater than zero");
        }

        if (discountPrice != null &&
                originalPrice.compareTo(discountPrice) <= 0) {
            throw new InvalidArgumentException("Original price must be greater than discount price");
        }

        if (discountRate != null && (discountRate < 0 || discountRate > 100)) {
            throw new InvalidArgumentException("Discount rate must be between 0 and 100");
        }
    }

    /**
     * Validates that the category name and link are unique before creation.
     */
    private void checkCategory(CategoryCreateRequest request) {
        if (categoryRepository.findByName(request.getName()).isPresent()) {
            log.warn("Category with name {} already exists", request.getName());
            throw new WearCategoryAlreadyExistsException("Category already exists with this name");
        }

        if (categoryRepository.findByLink(request.getLink()).isPresent()) {
            log.warn("Category with link {} already exists", request.getLink());
            throw new WearCategoryAlreadyExistsException("Category already exists with this link");
        }
    }

    /**
     * Constructs a dynamic JPA Specification based on the provided admin filter criteria.
     */
    private Specification<@NonNull Product> buildSpecification(ProductAdminFilterRequest filter) {
        return Specification
                .where(ProductSpecification.hasGender(filter.getGender()))
                .and(ProductSpecification.hasCategory(filter.getCategoryId()))
                .and(ProductSpecification.isActive(filter.getIsActive()))
                .and(ProductSpecification.isDeleted(filter.getIsDeleted()))
                .and(ProductSpecification.hasColor(filter.getColor()))
                .and(ProductSpecification.hasSize(filter.getProductSize()))
                .and(ProductSpecification.searchByTitleOrSkuOrDescription(filter.getKeyword()));
    }

    /**
     * Checks if a color option matches search criteria (keyword, specific color, or specific size).
     */
    private boolean isMatch(ColorOptionGetAllResponse color, String keyword, String filterColor, String filterSize) {
        // 1. Check specific color filter
        if (filterColor != null && !filterColor.isBlank()) {
            if (color.getColorName().toLowerCase().contains(filterColor)) return true;
        }

        // 2. Check specific size filter
        if (filterSize != null && !filterSize.isBlank()) {
            boolean sizeMatch = color.getItems().stream().anyMatch(item ->
                    item.getProductSize() != null &&
                            item.getProductSize().getSizeValue().toLowerCase().contains(filterSize));
            if (sizeMatch) return true;
        }

        // 3. Check general keyword
        if (keyword != null && !keyword.isBlank()) {
            String search = keyword.toLowerCase();
            if (color.getColorName().toLowerCase().contains(search)) return true;

            return color.getItems().stream().anyMatch(item -> {
                boolean skuMatches = item.getSku().toLowerCase().contains(search);
                boolean sizeMatches = item.getProductSize() != null &&
                        item.getProductSize().getSizeValue().toLowerCase().contains(search);
                return skuMatches || sizeMatches;
            });
        }

        return false;
    }

}
