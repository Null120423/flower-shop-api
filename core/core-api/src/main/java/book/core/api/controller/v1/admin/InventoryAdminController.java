package book.core.api.controller.v1.admin;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import book.core.api.controller.v1.admin.dto.inventory.InventoryResponseDto;
import book.core.api.controller.v1.admin.dto.inventory.InventoryUpdateDto;
import book.core.support.response.ApiResponse;
import book.storage.db.core.entity.InventoryEntity;
import book.storage.db.core.repository.InventoryRepository;
import book.storage.db.core.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Inventory Management", description = "API for managing product inventory and stock levels")
public class InventoryAdminController extends AdminV1Controller {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public InventoryAdminController(
            InventoryRepository inventoryRepository,
            ProductRepository productRepository
    ) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    // Get all inventory items
    @GetMapping("/inventory")
    @Operation(summary = "Get all inventory", description = "Retrieve all inventory items with pagination")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Inventory items retrieved successfully")
    })
    public ResponseEntity<ApiResponse<Page<InventoryResponseDto>>> getAllInventory(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InventoryEntity> inventory = inventoryRepository.findAll(pageable);
        
        Page<InventoryResponseDto> response = inventory.map(this::toResponseDto);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    // Get low stock products
    @GetMapping("/inventory/low-stock")
    @Operation(summary = "Get low stock products", description = "Retrieve products with stock levels below minimum threshold")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Low stock products retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<InventoryResponseDto>>> getLowStockProducts() {
        List<InventoryEntity> lowStock = inventoryRepository.findAll().stream()
                .filter(inv -> inv.getAvailableQuantity() <= inv.getMinStockLevel())
                .collect(Collectors.toList());
        
        List<InventoryResponseDto> response = lowStock.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    // Get inventory by product ID
    @GetMapping("/inventory/product/{productId}")
    @Operation(summary = "Get inventory by product", description = "Retrieve inventory details for a specific product")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Inventory retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product or inventory not found")
    })
    public ResponseEntity<ApiResponse<InventoryResponseDto>> getInventoryByProductId(
            @Parameter(description = "Product ID") @PathVariable UUID productId
    ) {
        return productRepository.findById(productId)
                .map(product -> {
                    if (product.getInventory() != null) {
                        return ResponseEntity.ok(ApiResponse.ok(toResponseDto(product.getInventory())));
                    }
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.<InventoryResponseDto>fail(null, "Inventory not found for this product"));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail(null, "Product not found")));
    }

    // Update inventory stock
    @PutMapping("/inventory/{id}")
    @Operation(summary = "Update inventory", description = "Update inventory stock quantity and minimum level")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Inventory updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Inventory not found")
    })
    public ResponseEntity<ApiResponse<InventoryResponseDto>> updateInventory(
            @Parameter(description = "Inventory ID") @PathVariable UUID id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated inventory data") 
            @Valid @RequestBody InventoryUpdateDto request
    ) {
        return inventoryRepository.findById(id)
                .map(inventory -> {
                    inventory.setStockQuantity(request.getStockQuantity());
                    if (request.getMinStockLevel() != null) {
                        inventory.setMinStockLevel(request.getMinStockLevel());
                    }
                    
                    InventoryEntity updated = inventoryRepository.save(inventory);
                    return ResponseEntity.ok(ApiResponse.ok(toResponseDto(updated)));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail(null, "Inventory not found")));
    }

    // Adjust stock (add or subtract)
    @PatchMapping("/inventory/{id}/adjust")
    @Operation(summary = "Adjust stock", description = "Add or subtract quantity from inventory stock")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Stock adjusted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid adjustment (negative stock)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Inventory not found")
    })
    public ResponseEntity<ApiResponse<InventoryResponseDto>> adjustStock(
            @Parameter(description = "Inventory ID") @PathVariable UUID id,
            @Parameter(description = "Quantity to adjust") @RequestParam int quantity,
            @Parameter(description = "Operation: add or subtract") @RequestParam(defaultValue = "add") String operation
    ) {
        return inventoryRepository.findById(id)
                .map(inventory -> {
                    int currentStock = inventory.getStockQuantity();
                    int newStock = operation.equalsIgnoreCase("add") 
                        ? currentStock + quantity 
                        : currentStock - quantity;
                    
                    if (newStock < 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(ApiResponse.<InventoryResponseDto>fail(null, "Stock cannot be negative"));
                    }
                    
                    inventory.setStockQuantity(newStock);
                    InventoryEntity updated = inventoryRepository.save(inventory);
                    return ResponseEntity.ok(ApiResponse.ok(toResponseDto(updated)));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail(null, "Inventory not found")));
    }

    private InventoryResponseDto toResponseDto(InventoryEntity inventory) {
        InventoryResponseDto dto = new InventoryResponseDto();
        dto.setId(inventory.getId());
        dto.setProductId(inventory.getProduct().getId());
        dto.setProductName(inventory.getProduct().getName());
        dto.setProductSku(inventory.getProduct().getSku());
        dto.setStockQuantity(inventory.getStockQuantity());
        dto.setReservedQuantity(inventory.getReservedQuantity());
        dto.setAvailableQuantity(inventory.getAvailableQuantity());
        dto.setMinStockLevel(inventory.getMinStockLevel());
        dto.setIsLowStock(inventory.getAvailableQuantity() <= inventory.getMinStockLevel());
        return dto;
    }
}
