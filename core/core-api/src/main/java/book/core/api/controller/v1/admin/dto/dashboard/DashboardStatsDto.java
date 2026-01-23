package book.core.api.controller.v1.admin.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDto {
    private SalesSummary salesSummary;
    private OrdersSummary ordersSummary;
    private ProductsSummary productsSummary;
    private CustomersSummary customersSummary;
    private List<TopProductDto> topSellingProducts;
    private List<LowStockProductDto> lowStockProducts;
    private List<RecentOrderDto> recentOrders;
}
