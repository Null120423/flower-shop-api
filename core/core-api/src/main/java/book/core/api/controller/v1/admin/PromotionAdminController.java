package book.core.api.controller.v1.admin;

import java.util.List;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import book.core.api.controller.v1.admin.dto.promotion.PromotionRequestDto;
import book.core.api.controller.v1.admin.dto.promotion.PromotionResponseDto;
import book.core.support.response.ApiResponse;
import book.storage.db.core.entity.ProductEntity;
import book.storage.db.core.entity.PromotionEntity;
import book.storage.db.core.repository.ProductRepository;
import book.storage.db.core.repository.PromotionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Promotion Management", description = "API for managing promotions and discount campaigns")
public class PromotionAdminController extends AdminV1Controller {

    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;

    public PromotionAdminController(
            PromotionRepository promotionRepository,
            ProductRepository productRepository
    ) {
        this.promotionRepository = promotionRepository;
        this.productRepository = productRepository;
    }

    // Get all promotions
    @GetMapping("/promotions")
    @Operation(summary = "Get all promotions", description = "Retrieve all promotions with optional active filter")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Promotions retrieved successfully")
    })
    public ResponseEntity<ApiResponse<Page<PromotionResponseDto>>> getAllPromotions(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Filter by active status") @RequestParam(required = false) Boolean active
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        Page<PromotionEntity> promotions;
        if (active != null) {
            promotions = promotionRepository.findByActive(active, pageable);
        } else {
            promotions = promotionRepository.findAll(pageable);
        }
        
        Page<PromotionResponseDto> response = promotions.map(this::toResponseDto);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    // Get promotion by ID
    @GetMapping("/promotions/{id}")
    @Operation(summary = "Get promotion by ID", description = "Retrieve a specific promotion by ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Promotion retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Promotion not found")
    })
    public ResponseEntity<ApiResponse<PromotionResponseDto>> getPromotionById(
            @Parameter(description = "Promotion ID") @PathVariable UUID id
    ) {
        return promotionRepository.findById(id)
                .map(promotion -> ResponseEntity.ok(ApiResponse.ok(toResponseDto(promotion))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail(null, "Promotion not found")));
    }

    // Create new promotion
    @PostMapping("/promotions")
    @Operation(summary = "Create new promotion", description = "Create a new promotion or discount campaign")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Promotion created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid promotion data")
    })
    public ResponseEntity<ApiResponse<PromotionResponseDto>> createPromotion(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Promotion details") 
            @Valid @RequestBody PromotionRequestDto request
    ) {
        PromotionEntity promotion = new PromotionEntity();
        updatePromotionFromDto(promotion, request);
        
        PromotionEntity saved = promotionRepository.save(promotion);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(toResponseDto(saved)));
    }

    // Update promotion
    @PutMapping("/promotions/{id}")
    @Operation(summary = "Update promotion", description = "Update an existing promotion")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Promotion updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Promotion not found")
    })
    public ResponseEntity<ApiResponse<PromotionResponseDto>> updatePromotion(
            @Parameter(description = "Promotion ID") @PathVariable UUID id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated promotion details") 
            @Valid @RequestBody PromotionRequestDto request
    ) {
        return promotionRepository.findById(id)
                .map(promotion -> {
                    updatePromotionFromDto(promotion, request);
                    PromotionEntity updated = promotionRepository.save(promotion);
                    return ResponseEntity.ok(ApiResponse.ok(toResponseDto(updated)));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail(null, "Promotion not found")));
    }

    // Toggle promotion active status
    @PatchMapping("/promotions/{id}/toggle")
    @Operation(summary = "Toggle promotion status", description = "Quickly enable or disable a promotion")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Promotion toggled successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Promotion not found")
    })
    public ResponseEntity<ApiResponse<PromotionResponseDto>> togglePromotion(
            @Parameter(description = "Promotion ID") @PathVariable UUID id
    ) {
        return promotionRepository.findById(id)
                .map(promotion -> {
                    promotion.setActive(!promotion.getActive());
                    PromotionEntity updated = promotionRepository.save(promotion);
                    return ResponseEntity.ok(ApiResponse.ok(toResponseDto(updated)));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail(null, "Promotion not found")));
    }

    // Delete promotion (soft delete)
    @DeleteMapping("/promotions/{id}")
    @Operation(summary = "Delete promotion", description = "Delete a promotion (soft delete)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Promotion deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Promotion not found")
    })
    public ResponseEntity<ApiResponse<Void>> deletePromotion(
            @Parameter(description = "Promotion ID") @PathVariable UUID id
    ) {
        return promotionRepository.findById(id)
                .map(promotion -> {
                    promotionRepository.delete(promotion);
                    return ResponseEntity.ok(ApiResponse.ok());
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail(null, "Promotion not found")));
    }

    // Get active promotions
    @GetMapping("/promotions/active")
    @Operation(summary = "Get active promotions", description = "Retrieve all currently active promotions")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Active promotions retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<PromotionResponseDto>>> getActivePromotions() {
        List<PromotionEntity> active = promotionRepository.findByActive(
                true, 
                PageRequest.of(0, 100)
        ).getContent();
        
        List<PromotionResponseDto> response = active.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    private void updatePromotionFromDto(PromotionEntity promotion, PromotionRequestDto dto) {
        promotion.setCode(dto.getCode());
        promotion.setName(dto.getName());
        promotion.setDescription(dto.getDescription());
        promotion.setType(dto.getType());
        promotion.setValue(dto.getValue());
        promotion.setStartAt(dto.getStartAt());
        promotion.setEndAt(dto.getEndAt());
        promotion.setActive(dto.getActive() != null ? dto.getActive() : true);
        promotion.setUsageLimit(dto.getUsageLimit());
        
        // Update applicable products
        if (dto.getProductIds() != null && !dto.getProductIds().isEmpty()) {
            Set<ProductEntity> products = dto.getProductIds().stream()
                    .map(id -> productRepository.findById(UUID.fromString(id)))
                    .filter(opt -> opt.isPresent())
                    .map(opt -> opt.get())
                    .collect(Collectors.toSet());
            promotion.setProducts(products);
        }
    }

    private PromotionResponseDto toResponseDto(PromotionEntity promotion) {
        PromotionResponseDto dto = new PromotionResponseDto();
        dto.setId(promotion.getId());
        dto.setCode(promotion.getCode());
        dto.setName(promotion.getName());
        dto.setDescription(promotion.getDescription());
        dto.setType(promotion.getType());
        dto.setValue(promotion.getValue());
        dto.setStartAt(promotion.getStartAt());
        dto.setEndAt(promotion.getEndAt());
        dto.setActive(promotion.getActive());
        dto.setUsageLimit(promotion.getUsageLimit());
        dto.setUsageCount(promotion.getUsageCount());
        dto.setApplicableProductCount(promotion.getProducts().size());
        dto.setCreatedAt(promotion.getCreatedAt());
        dto.setUpdatedAt(promotion.getUpdatedAt());
        return dto;
    }
}
