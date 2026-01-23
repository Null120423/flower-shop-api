package book.core.api.controller.v1.admin;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import book.core.api.controller.v1.admin.dto.product.ProductRequestDto;
import book.core.api.controller.v1.admin.dto.product.ProductResponseDto;
import book.core.support.response.ApiResponse;
import book.storage.db.core.entity.CategoryEntity;
import book.storage.db.core.entity.InventoryEntity;
import book.storage.db.core.entity.ProductEntity;
import book.storage.db.core.repository.CategoryRepository;
import book.storage.db.core.repository.InventoryRepository;
import book.storage.db.core.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Product Management", description = "API for managing flower products")
public class ProductAdminController extends AdminV1Controller {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryRepository inventoryRepository;

    public ProductAdminController(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            InventoryRepository inventoryRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.inventoryRepository = inventoryRepository;
    }

    // Get all products with pagination
    @GetMapping("/products")
    @Operation(summary = "Get all products", description = "Retrieve all flower products with pagination and sorting")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public ResponseEntity<ApiResponse<Page<ProductResponseDto>>> getAllProducts(
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction (ASC/DESC)") @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        Sort sort = sortDirection.equalsIgnoreCase("ASC") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductEntity> products = productRepository.findAll(pageable);
        
        Page<ProductResponseDto> response = products.map(this::toResponseDto);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    // Get product by ID
    @GetMapping("/products/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductById(
            @Parameter(description = "Product ID") @PathVariable UUID id
    ) {
        return productRepository.findById(id)
                .map(product -> ResponseEntity.ok(ApiResponse.ok(toResponseDto(product))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail(null, "Product not found")));
    }

    // Create new product
    @PostMapping("/products")
    @Operation(summary = "Create new product", description = "Create a new flower product with inventory tracking")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Product created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid product data")
    })
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product details", required = true)
            @Valid @RequestBody ProductRequestDto request
    ) {
        ProductEntity product = new ProductEntity();
        updateProductFromDto(product, request);
        
        ProductEntity saved = productRepository.save(product);
        
        // Create inventory if stock data is provided
        if (request.getStockQuantity() != null) {
            InventoryEntity inventory = new InventoryEntity();
            inventory.setProduct(saved);
            inventory.setStockQuantity(request.getStockQuantity());
            inventory.setMinStockLevel(request.getMinStockLevel() != null ? request.getMinStockLevel() : 0);
            inventoryRepository.save(inventory);
        }
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(toResponseDto(saved)));
    }

    // Update product
    @PutMapping("/products/{id}")
    @Operation(summary = "Update product", description = "Update an existing product details")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ApiResponse<ProductResponseDto>> updateProduct(
            @Parameter(description = "Product ID") @PathVariable UUID id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated product details") 
            @Valid @RequestBody ProductRequestDto request
    ) {
        return productRepository.findById(id)
                .map(product -> {
                    updateProductFromDto(product, request);
                    ProductEntity updated = productRepository.save(product);
                    
                    // Update inventory if exists
                    if (request.getStockQuantity() != null && updated.getInventory() != null) {
                        InventoryEntity inventory = updated.getInventory();
                        inventory.setStockQuantity(request.getStockQuantity());
                        if (request.getMinStockLevel() != null) {
                            inventory.setMinStockLevel(request.getMinStockLevel());
                        }
                        inventoryRepository.save(inventory);
                    }
                    
                    return ResponseEntity.ok(ApiResponse.ok(toResponseDto(updated)));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail(null, "Product not found")));
    }

    // Delete product (soft delete)
    @DeleteMapping("/products/{id}")
    @Operation(summary = "Delete product", description = "Delete a product (soft delete)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @Parameter(description = "Product ID") @PathVariable UUID id
    ) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return ResponseEntity.ok(ApiResponse.ok());
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail(null, "Product not found")));
    }

    // Search products by name or SKU
    @GetMapping("/products/search")
    @Operation(summary = "Search products", description = "Search products by name or SKU")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid search parameters")
    })
    public ResponseEntity<ApiResponse<Page<ProductResponseDto>>> searchProducts(
            @Parameter(description = "Search query (name or SKU)") @RequestParam String query,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductEntity> products = productRepository.findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(
                query, query, pageable);
        
        Page<ProductResponseDto> response = products.map(this::toResponseDto);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    private void updateProductFromDto(ProductEntity product, ProductRequestDto dto) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setSku(dto.getSku());
        product.setType(dto.getType());
        product.setImageUrl(dto.getImageUrl());
        
        // Update categories
        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            Set<CategoryEntity> categories = dto.getCategoryIds().stream()
                    .map(id -> categoryRepository.findById(UUID.fromString(id)))
                    .filter(opt -> opt.isPresent())
                    .map(opt -> opt.get())
                    .collect(Collectors.toSet());
            product.setCategories(categories);
        }
    }

    private ProductResponseDto toResponseDto(ProductEntity product) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setSku(product.getSku());
        dto.setType(product.getType());
        dto.setImageUrl(product.getImageUrl());
        dto.setCategoryNames(product.getCategories().stream()
                .map(CategoryEntity::getName)
                .collect(Collectors.toSet()));
        
        if (product.getInventory() != null) {
            dto.setStockQuantity(product.getInventory().getStockQuantity());
            dto.setAvailableQuantity(product.getInventory().getAvailableQuantity());
            dto.setMinStockLevel(product.getInventory().getMinStockLevel());
        }
        
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        
        return dto;
    }
}
