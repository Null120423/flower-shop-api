package book.core.api.controller.v1.admin.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LowStockProductDto {
    private UUID productId;
    private String productName;
    private String productSku;
    private Integer stockQuantity;
    private Integer minStockLevel;
}
