package book.core.api.controller.v1.admin.dto.product;

import book.core.enums.ProductType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    
    @NotBlank(message = "Product name is required")
    @Size(max = 150, message = "Product name must not exceed 150 characters")
    private String name;
    
    private String description;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;
    
    @NotBlank(message = "SKU is required")
    @Size(max = 64, message = "SKU must not exceed 64 characters")
    private String sku;
    
    @NotNull(message = "Product type is required")
    private ProductType type;
    
    private String imageUrl;
    
    private Set<String> categoryIds;
    
    // Inventory data (optional, will create/update inventory)
    private Integer stockQuantity;
    private Integer minStockLevel;
}
