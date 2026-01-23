package book.core.api.controller.v1.admin.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductsSummary {
    private Long totalProducts;
    private Long lowStockProducts;
    private Long outOfStockProducts;
}
