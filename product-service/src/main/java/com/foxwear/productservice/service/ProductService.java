package com.foxwear.productservice.service;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.common.dto.response.ProductResponse;
import com.foxwear.common.exception.InvalidArgumentException;
import com.foxwear.common.utils.CodeGenerator;
import com.foxwear.common.utils.StringHelper;
import com.foxwear.productservice.client.InteractionClient;
import com.foxwear.productservice.dto.request.*;
import com.foxwear.productservice.dto.response.*;
import com.foxwear.productservice.entity.*;
import com.foxwear.productservice.exception.*;
import com.foxwear.productservice.mapper.*;
import com.foxwear.productservice.repository.*;
import com.foxwear.productservice.repository.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final WearCategoryRepository categoryRepository;
    private final ProductSizeRepository productSizeRepository;
    private final ColorOptionRepository colorOptionRepository;
    private final ProductItemRepository productItemRepository;
    private final InteractionClient interactionClient;
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final SizeMapper sizeMapper;
    private final ColorMapper colorMapper;
    private final ItemMapper itemMapper;
    private final ImageMapper imageMapper;

    /**
     * Creates a new product including its category association and color options.
     */
    @Transactional
    public ProductCreateResponse createProduct(ProductCreateRequest request) {
        log.info("Creating new product with title: {}", request.getTitle());
        checkPrices(request);

        // 1. Validate and retrieve the category
        WearCategory category = findCategoryOrThrow(request.getCategoryId());

        // 2. Map request to Product entity and set basic details
        Product product = productMapper.toEntity(request);
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
     * Creates a new product category, optionally associating it with a parent category.
     */
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        log.info("Creating new category with name: {}", request.getName());
        checkCategoryUniquenessForCreate(request);

        WearCategory category = categoryMapper.toEntity(request);
        category.setId(null);
        category.setName(StringHelper.capitalize(request.getName()));

        if (request.getParentId() != null) {
            WearCategory parent = findCategoryOrThrow(request.getParentId());
            category.setParent(parent);
        }

        WearCategory savedCategory = categoryRepository.save(category);
        log.info("Category created successfully with ID: {}", savedCategory.getId());

        return getCategoryResponse(savedCategory);
    }

    /**
     * Creates a new product size definition.
     */
    @Transactional
    public SizeResponse createSize(SizeRequest request) {
        log.info("Creating new size with value: {}", request.getSizeValue());

        if (productSizeRepository.existsBySizeValue(request.getSizeValue())) {
            log.warn("Size with value {} already exists", request.getSizeValue());
            throw new SizeAlreadyExistsException("Size already exists");
        }

        ProductSize size = sizeMapper.toEntity(request);
        size.setId(null);

        var savedSize = productSizeRepository.save(size);
        log.info("Size created successfully with ID: {}", savedSize.getId());

        return sizeMapper.toResponse(savedSize);
    }

    /**
     * Retrieves a paginated list of products filtered by admin-defined criteria.
     */
    @Transactional(readOnly = true)
    public Page<ProductGetAllResponse> getAllProductWithAdminFilter(ProductAdminFilterRequest filter) {
        log.info("Fetching products with admin filter: {}", filter);
        Pageable pageable = PageRequest.of(
                filter.getPage(),
                filter.getSize(),
                Sort.by(filter.getDirection(), filter.getSortBy())
        );

        var products = productRepository.findAll(buildAdminRequestSpecification(filter), pageable);

        return getFilterResponse(products, filter.getKeyword(), filter.getColor(), filter.getProductSize(), new HashSet<>());
    }

    /**
     * Retrieves a paginated list of products filtered by user-defined criteria.
     */
    @Transactional(readOnly = true)
    public Page<ProductGetAllResponse> getAllProductWithUserFilter(ProductUserFilterRequest filter, Long userId) {
        log.info("Fetching products with user filter: {}", filter);
        ApiResponse<Set<Long>> response = interactionClient.getMyLikedIds(userId);
        Set<Long> likedIds = (response != null && response.getData() != null)
                ? response.getData()
                : Collections.emptySet();

        Pageable pageable = PageRequest.of(
                filter.getPage(),
                filter.getSize(),
                Sort.by(filter.getDirection(), filter.getSortBy())
        );

        var products = productRepository.findAll(buildUserRequestSpecification(filter), pageable);

        return getFilterResponse(products, filter.getKeyword(), filter.getColor(), filter.getProductSize(), likedIds);
    }

    /**
     * Retrieves a detailed view of a single product by its slug.
     */
    @Transactional(readOnly = true)
    public ProductGetResponse getProductWithSlug(String slug, Long userId) {
        log.info("Fetching detailed product information for slug: {}", slug);
        Product product = findProductOrThrow(slug);

        ProductGetResponse productResponse = productMapper.toGetResponse(product);

        ApiResponse<Set<Long>> response = interactionClient.getMyLikedIds(userId);

        Set<Long> likedIds = (response != null && response.getData() != null)
                ? response.getData()
                : Collections.emptySet();

        productResponse.setLiked(likedIds.contains(product.getId()));

        productResponse.setCategory(
                categoryMapper.toResponse(product.getCategory())
        );
        productResponse.setColors(product.getColors().stream()
                .map(this::getColorGetResponse)
                .toList());

        return productResponse;
    }

    /**
     * Retrieves basic product information associated with a specific product item ID.
     * This is typically used for order processing or cart displays.
     */
    @Transactional(readOnly = true)
    public ProductResponse getProductWithItemId(Long itemId) {
        log.info("Fetching product information for item ID: {}", itemId);
        ProductItem item = findProductItemOrThrow(itemId);
        Product product = item.getColorOption().getProduct();

        ProductResponse response = productMapper.toResponse(product);
        response.setImageUrl(
                item.getColorOption()
                        .getImages().stream()
                        .filter(ColorOptionImage::isMain)
                        .findFirst()
                        .get().getImage()
        );
        response.setColor(item.getColorOption().getColorName());
        response.setSize(item.getProductSize().getSizeValue());

        return response;
    }

    /**
     * Retrieves all unique color options used in the catalog.
     */
    @Transactional(readOnly = true)
    public List<ColorOptionAllValuesResponse> getAllColorOptionsValues() {
        log.info("Fetching all unique color option values");
        var colors = colorOptionRepository.findAllUniqueColorNames();

        return colors.stream()
                .map(colorMapper::toGetAllValuesResponse)
                .toList();
    }

    /**
     * Retrieves a list of all available product categories.
     */
    @Transactional(readOnly = true)
    public List<CategoryGetAllResponse> getAllCategories() {
        log.info("Fetching all categories");
        List<WearCategory> categories = categoryRepository.findAllByParentIsNotNull();

        return categories.stream()
                .map(this::getAllCategoryResponse)
                .toList();
    }

    /**
     * Retrieves a category by its unique identifier.
     */
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        log.info("Fetching category with ID: {}", id);
        WearCategory category = findCategoryOrThrow(id);

        return getCategoryResponse(category);
    }

    /**
     * Retrieves all available product size definitions.
     */
    @Transactional(readOnly = true)
    public List<SizeResponse> getAllSizes() {
        log.info("Fetching all sizes");
        return productSizeRepository.findAll(
                        Sort.by(Sort.Direction.ASC, "id")
                ).stream()
                .map(sizeMapper::toResponse)
                .toList();
    }

    /**
     * Retrieves a specific product size definition by its ID.
     */
    @Transactional(readOnly = true)
    public SizeResponse getSizeById(Long id) {
        log.info("Fetching size with ID: {}", id);
        ProductSize size = findProductSizeOrThrow(id);

        return sizeMapper.toResponse(size);
    }

    /**
     * Retrieves the top 10 products with the highest number of likes.
     */
    @Transactional(readOnly = true)
    public List<ProductGetAllResponse> getMostLiked(Long userId) {
        log.info("Fetching top 10 most liked products");
        var products = productRepository.findTop10MostLiked();

        Set<Long> likedIds = Collections.emptySet();

        try {
            ApiResponse<Set<Long>> response = interactionClient.getMyLikedIds(userId);
            likedIds = (response != null && response.getData() != null)
                    ? response.getData()
                    : Collections.emptySet();
        } catch (Exception ex) {
            log.error("Error fetching liked ids from interaction service: {}", ex.getMessage());
        }

        Set<Long> finalLikedIds = likedIds;

        return products.stream()
                .map(product -> {
                    ProductGetAllResponse productResponse = productMapper.toGetAllResponse(product);
                    productResponse.setLiked(finalLikedIds.contains(product.getId()));
                    productResponse.setCategoryName(product.getCategory().getName());
                    productResponse.setColors(product.getColors().stream()
                            .map(this::getColorGetAllResponse)
                            .collect(Collectors.toCollection(ArrayList::new)));

                    return productResponse;
                })
                .toList();
    }

    public BigDecimal getProductPrice(Long itemId) {
        ProductItem item = findProductItemOrThrow(itemId);
        Product product = item.getColorOption().getProduct();

        return product.getDiscountPrice() != null ? product.getDiscountPrice() : product.getOriginalPrice();
    }

    /**
     * Updates an existing product's details and its color options.
     */
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
     * Updates an existing product category's details, including its parent association.
     */
    @Transactional
    public CategoryResponse updateCategory(CategoryRequest request, Long id) {
        log.info("Updating category ID: {} with name: {}", id, request.getName());
        WearCategory category = findCategoryOrThrow(id);

        checkCategoryUniquenessForUpdate(request, id);

        category.setName(StringHelper.capitalize(request.getName()));
        category.setSubtitle(request.getSubtitle());
        category.setLink(request.getLink());
        category.setMainImage(request.getMainImage());

        if (request.getParentId() != null) {
            if (request.getParentId().equals(id)) {
                throw new InvalidArgumentException("A category cannot be its own parent");
            }
            WearCategory parent = findCategoryOrThrow(request.getParentId());
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        WearCategory savedCategory = categoryRepository.save(category);
        log.info("Category updated successfully with ID: {}", savedCategory.getId());

        return getCategoryResponse(savedCategory);
    }

    /**
     * Updates an existing product size definition.
     */
    @Transactional
    public SizeResponse updateSize(SizeRequest request, Long id) {
        log.info("Updating size ID: {} with value: {}", id, request.getSizeValue());
        if (productSizeRepository.existsBySizeValueAndIdNot(request.getSizeValue(), id)) {
            log.warn("Product size already exists with name {}", request.getSizeValue());
            throw new ProductSizeAlreadyExistException("Product size already exists with name " + request.getSizeValue());
        }

        ProductSize size = findProductSizeOrThrow(id);
        size.setSizeValue(request.getSizeValue());

        log.info("Size updated successfully with ID: {}", size.getId());
        return sizeMapper.toResponse(size);
    }

    /**
     * Updates the activation status of an existing product.
     */
    @Transactional
    public void updateProductActivity(Long id) {
        Product product = findProductOrThrow(id);
        log.info("Updating activity status for product ID: {} to {}", id, !product.isActive());

        product.setActive(!product.isActive());
    }

    /**
     * Marks a product as deleted (soft delete) to preserve referential integrity.
     */
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
    @Transactional
    public void deleteProduct(Long id) {
        log.info("Hard deleting product ID: {}", id);
        Product product = findProductOrThrow(id);

        productRepository.delete(product);
        log.info("Product hard deleted successfully with ID: {}", id);
    }

    /**
     * Permanently removes a product size definition from the database.
     */
    @Transactional
    public void deleteSize(Long id) {
        log.info("Deleting product size ID: {}", id);
        ProductSize size = findProductSizeOrThrow(id);

        productSizeRepository.delete(size);
        log.info("Product size deleted successfully with ID: {}", id);
    }

    @Transactional
    public void deleteCategory(Long id) {
        log.info("Deleting category ID: {}", id);
        WearCategory category = findCategoryOrThrow(id);

        categoryRepository.delete(category);
        log.info("Category deleted successfully with ID: {}", id);
    }

    /**
     * Updates the stock quantity for a specific product item.
     */
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

        return itemMapper.toUpdateResponse(productItem);
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
     * Helper method to find a {@link Product} by slug or throw a {@link ProductNotFoundException}.
     */
    private Product findProductOrThrow(String slug) {
        return productRepository.findBySlug(slug)
                .orElseThrow(() -> {
                    log.error("Product not found with slug: {}", slug);
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
     * Helper method to find a {@link ProductSize} by ID or throw a {@link ProductSizeNotFoundException}.
     */
    private ProductSize findProductSizeOrThrow(Long id) {
        return productSizeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product size not found with ID: {}", id);
                    return new ProductSizeNotFoundException("Product size not found");
                });
    }

    /**
     * Maps a {@link ColorOptionCreateRequest} to a {@link ColorOption} entity, including nested images and items.
     */
    private ColorOption mapToColorOption(ColorOptionDTO colorOptionDTO, Product product) {
        ColorOption colorOption = colorMapper.toEntity(colorOptionDTO);

        colorOption.setId(null);
        colorOption.setProduct(product);
        colorOption.setColorName(StringHelper.capitalize(colorOption.getColorName()));
        colorOption.setColorCode(colorOption.getColorCode().trim());

        // Map nested images and items using helper methods
        List<ColorOptionImage> images = mapImagesToEntityList(colorOptionDTO, colorOption);
        List<ProductItem> items = mapItemsToEntityList(colorOptionDTO, colorOption);

        colorOption.setImages(images);
        colorOption.setItems(items);

        return colorOption;
    }

    /**
     * Maps image requests to {@link ColorOptionImage} entities and associates them with a {@link ColorOption}.
     */
    private List<ColorOptionImage> mapImagesToEntityList(ColorOptionDTO color, ColorOption colorOption) {
        return color.getImages().stream()
                .map(image -> {
                    ColorOptionImage colorOptionImage = imageMapper.toEntity(image);
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
    private List<ProductItem> mapItemsToEntityList(ColorOptionDTO color, ColorOption colorOption) {
        return color.getItems().stream()
                .map(item -> {
                    ProductSize productSize = findProductSizeOrThrow(item.getSizeId());

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
        ProductCreateResponse response = productMapper.toCreateResponse(product);
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
        ProductUpdateResponse response = productMapper.toUpdateResponse(product);
        response.setCategoryName(product.getCategory().getName());

        List<ColorOptionUpdateResponse> colors = product.getColors().stream()
                .map(this::getColorUpdateResponse)
                .toList();

        response.setColors(colors);

        return response;
    }

    /**
     * Transforms a {@link Product} entity into a {@link ProductGetAllResponse} DTO.
     * If a specific color or size filter is applied, the matching color option is moved to the
     * front of the list to ensure the relevant variant is displayed first.
     */
    private ProductGetAllResponse getProductGetAllResponse(Product product, String searchKeyword, List<String> filterColors, List<String> filterSizes, Set<Long> likedIds) {
        ProductGetAllResponse productResponse = productMapper.toGetAllResponse(product);
        productResponse.setCategoryName(product.getCategory().getName());
        productResponse.setLiked(likedIds.contains(product.getId()));
        productResponse.setColors(product.getColors().stream()
                .map(this::getColorGetAllResponse)
                .collect(Collectors.toCollection(ArrayList::new)));


        if (productResponse.getColors() != null) {
            var matchingColor = productResponse.getColors().stream()
                    .filter(c -> isMatchColors(c, searchKeyword, filterColors, filterSizes))
                    .findFirst();

            matchingColor.ifPresent(color -> {
                productResponse.getColors().remove(color);
                productResponse.getColors().addFirst(color);
            });
        }

        return productResponse;
    }

    /**
     * Processes a page of products to convert them into DTOs, ensuring that matching variants
     * are prioritized in the response based on the search criteria.
     */
    private Page<ProductGetAllResponse> getFilterResponse(Page<Product> products, String keyword, List<String> colors, List<String> productSizes, Set<Long> likedIds) {
        String searchKeyword = keyword == null ? null : keyword.toLowerCase();
        List<String> filterColors = colors == null ? null : colors.stream().map(String::toLowerCase).toList();
        List<String> filterSizes = productSizes == null ? null : productSizes.stream().map(String::toLowerCase).toList();

        return products.map(p ->
                getProductGetAllResponse(p, searchKeyword, filterColors, filterSizes, likedIds));
    }

    /**
     * Transforms a {@link ColorOption} entity and its nested images and items into a {@link ColorOptionCreateResponse} DTO.
     */
    private ColorOptionCreateResponse getColorCreateResponse(ColorOption color) {
        ColorOptionCreateResponse colorResponse = colorMapper.toCreateResponse(color);

        List<ImageCreateResponse> images = color.getImages().stream()
                .map(image -> {
                    ImageCreateResponse imageResponse = imageMapper.toCreateResponse(image);
                    imageResponse.setMain(image.isMain());

                    return imageResponse;
                })
                .toList();

        List<ItemCreateResponse> items = color.getItems().stream()
                .map(itemMapper::toCreateResponse)
                .toList();

        colorResponse.setImages(images);
        colorResponse.setItems(items);

        return colorResponse;
    }

    /**
     * Transforms a {@link ColorOption} entity and its nested images and items into a {@link ColorOptionUpdateResponse} DTO.
     */
    private ColorOptionUpdateResponse getColorUpdateResponse(ColorOption color) {
        ColorOptionUpdateResponse colorResponse = colorMapper.toUpdateResponse(color);

        List<ImageUpdateResponse> images = color.getImages().stream()
                .map(image -> {
                    ImageUpdateResponse imageResponse = imageMapper.toUpdateResponse(image);
                    imageResponse.setMain(image.isMain());

                    return imageResponse;
                })
                .toList();

        List<ItemUpdateResponse> items = color.getItems().stream()
                .map(itemMapper::toUpdateResponse)
                .toList();

        colorResponse.setImages(images);
        colorResponse.setItems(items);

        return colorResponse;
    }

    /**
     * Transforms a {@link ColorOption} entity into a {@link ColorOptionGetAllResponse} DTO for list views.
     */
    private ColorOptionGetAllResponse getColorGetAllResponse(ColorOption color) {
        ColorOptionGetAllResponse colorResponse = colorMapper.toGetAllResponse(color);
        var images = color.getImages().stream()
                .map(imageMapper::toGetAllResponse)
                .toList();

        var items = color.getItems().stream()
                .map(itemMapper::toGetAllResponse)
                .toList();

        colorResponse.setImages(images);
        colorResponse.setItems(items);

        return colorResponse;
    }

    /**
     * Transforms a {@link ColorOption} entity into a detailed {@link ColorOptionGetResponse} DTO.
     */
    private ColorOptionGetResponse getColorGetResponse(ColorOption color) {
        ColorOptionGetResponse colorResponse = colorMapper.toGetResponse(color);
        var images = color.getImages().stream()
                .map(imageMapper::toGetResponse)
                .toList();

        var items = color.getItems().stream()
                .map(itemMapper::toGetResponse)
                .toList();

        colorResponse.setImages(images);
        colorResponse.setItems(items);

        return colorResponse;
    }

    /**
     * Transforms a {@link WearCategory} entity and its parent into a {@link CategoryResponse} DTO.
     */
    private CategoryResponse getCategoryResponse(WearCategory category) {
        CategoryResponse categoryResponse = categoryMapper.toResponse(category);
        categoryResponse.setParent(
                category.getParent() != null ? categoryMapper.toResponse(category.getParent()) : null
        );

        return categoryResponse;
    }

    private CategoryGetAllResponse getAllCategoryResponse(WearCategory category) {
        CategoryGetAllResponse categoryResponse = categoryMapper.toGetAllResponse(category);
        categoryResponse.setParentName(
                category.getParent() != null ? category.getParent().getName() : null
        );

        return categoryResponse;
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
                originalPrice.compareTo(discountPrice) < 0) {
            throw new InvalidArgumentException("Original price must be greater than discount price");
        }

        if (discountRate != null && (discountRate < 0 || discountRate > 100)) {
            throw new InvalidArgumentException("Discount rate must be between 0 and 100");
        }
    }

    /**
     * Validates that the category name and link are unique before updating.
     */
    private void checkCategoryUniquenessForUpdate(CategoryRequest request, Long id) {
        if (categoryRepository.existsByNameAndIdNot(request.getName(), id)) {
            log.warn("Category with name {} already exists", request.getName());
            throw new WearCategoryAlreadyExistsException("Category already exists with this name");
        }

        if (request.getLink() != null && categoryRepository.existsByLinkAndIdNot(request.getLink(), id)) {
            log.warn("Category with link {} already exists", request.getLink());
            throw new WearCategoryAlreadyExistsException("Category already exists with this link");
        }
    }

    /**
     * Validates that the category name and link are unique before creation.
     */
    private void checkCategoryUniquenessForCreate(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            log.warn("Category with name: {} already exists", request.getName());
            throw new WearCategoryAlreadyExistsException("Category already exists with this name");
        }

        if (request.getLink() != null && categoryRepository.existsByLink(request.getLink())) {
            log.warn("Category with link: {} already exists", request.getLink());
            throw new WearCategoryAlreadyExistsException("Category already exists with this link");
        }
    }

    /**
     * Constructs a dynamic JPA Specification based on the provided admin filter criteria.
     */
    private Specification<Product> buildAdminRequestSpecification(ProductAdminFilterRequest filter) {
        return buildBasicSpecification(filter)
                .and(ProductSpecification.isActive(filter.getIsActive()))
                .and(ProductSpecification.isDeleted(filter.getIsDeleted()));
    }

    /**
     * Constructs a dynamic JPA Specification for user-facing product requests.
     */
    private Specification<Product> buildUserRequestSpecification(ProductUserFilterRequest filter) {
        return buildBasicSpecification(filter)
                .and(ProductSpecification.isActive(true))
                .and(ProductSpecification.isDeleted(false));
    }

    /**
     * Constructs the base JPA Specification shared between admin and user filters,
     * covering common criteria like gender, category, color, size, keyword search, and price range.
     */
    private Specification<Product> buildBasicSpecification(FilterDTO filter) {
        return Specification
                .where(ProductSpecification.hasGender(filter.getGender()))
                .and(ProductSpecification.hasCategory(filter.getCategoryId()))
                .and(ProductSpecification.hasColor(filter.getColor()))
                .and(ProductSpecification.hasSize(filter.getProductSize()))
                .and(ProductSpecification.searchByTitleOrSkuOrDescription(filter.getKeyword()))
                .and(ProductSpecification.hasPriceBetween(filter.getMinPrice(), filter.getMaxPrice()));
    }

    /**
     * Determines if a specific color option matches the provided filtering or search criteria.
     * This logic is used to prioritize which product variant is displayed first in search results.
     */
    private boolean isMatchColors(ColorOptionGetAllResponse color, String keyword, List<String> filterColors, List<String> filterSizes) {
        // 1. Check specific color filter
        if (filterColors != null && !filterColors.isEmpty()) {
            if (filterColors.contains(color.getColorName().toLowerCase())) return true;
        }

        // 2. Check specific size filter
        if (filterSizes != null && !filterSizes.isEmpty()) {
            boolean sizeMatch = color.getItems().stream().anyMatch(item ->
                    item.getProductSize() != null &&
                            filterSizes.contains(item.getProductSize().getSizeValue().toLowerCase()));
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
