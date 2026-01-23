package book.core.api.controller.v1.admin.dto.product;

import book.core.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private String sku;
    private ProductType type;
    private String imageUrl;
    private Set<String> categoryNames;
    private Integer stockQuantity;
    private Integer availableQuantity;
    private Integer minStockLevel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
