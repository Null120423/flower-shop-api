package book.core.api.controller.v1.admin;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import book.core.api.controller.v1.admin.dto.category.CategoryRequestDto;
import book.core.api.controller.v1.admin.dto.category.CategoryResponseDto;
import book.core.support.response.ApiResponse;
import book.storage.db.core.entity.CategoryEntity;
import book.storage.db.core.repository.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Category Management", description = "API for managing product categories")
public class CategoryAdminController extends AdminV1Controller {

    private final CategoryRepository categoryRepository;

    public CategoryAdminController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Get all categories
    @GetMapping("/categories")
    @Operation(summary = "Get all categories", description = "Retrieve all product categories")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> getAllCategories() {
        List<CategoryEntity> categories = categoryRepository.findAll(Sort.by("name").ascending());
        List<CategoryResponseDto> response = categories.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    // Get category by ID
    @GetMapping("/categories/{id}")
    @Operation(summary = "Get category by ID", description = "Retrieve a specific category by its ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<ApiResponse<CategoryResponseDto>> getCategoryById(
            @Parameter(description = "Category ID") @PathVariable UUID id
    ) {
        return categoryRepository.findById(id)
                .map(category -> ResponseEntity.ok(ApiResponse.ok(toResponseDto(category))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail(null, "Category not found")));
    }

    // Create new category
    @PostMapping("/categories")
    @Operation(summary = "Create new category", description = "Create a new product category")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Category created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid category data")
    })
    public ResponseEntity<ApiResponse<CategoryResponseDto>> createCategory(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Category details") 
            @Valid @RequestBody CategoryRequestDto request
    ) {
        CategoryEntity category = new CategoryEntity();
        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category.setDescription(request.getDescription());
        
        CategoryEntity saved = categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(toResponseDto(saved)));
    }

    // Update category
    @PutMapping("/categories/{id}")
    @Operation(summary = "Update category", description = "Update an existing category")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<ApiResponse<CategoryResponseDto>> updateCategory(
            @Parameter(description = "Category ID") @PathVariable UUID id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated category details") 
            @Valid @RequestBody CategoryRequestDto request
    ) {
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setName(request.getName());
                    category.setSlug(request.getSlug());
                    category.setDescription(request.getDescription());
                    
                    CategoryEntity updated = categoryRepository.save(category);
                    return ResponseEntity.ok(ApiResponse.ok(toResponseDto(updated)));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail(null, "Category not found")));
    }

    // Delete category (soft delete)
    @DeleteMapping("/categories/{id}")
    @Operation(summary = "Delete category", description = "Delete a category (soft delete)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @Parameter(description = "Category ID") @PathVariable UUID id
    ) {
        return categoryRepository.findById(id)
                .map(category -> {
                    categoryRepository.delete(category);
                    return ResponseEntity.ok(ApiResponse.ok());
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail(null, "Category not found")));
    }

    private CategoryResponseDto toResponseDto(CategoryEntity category) {
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setSlug(category.getSlug());
        dto.setDescription(category.getDescription());
        dto.setProductCount(category.getProducts().size());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        return dto;
    }
}

