package book.core.api.controller.v1.admin.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersSummary {
    private Long totalOrders;
    private Long pendingOrders;
    private Long processingOrders;
    private Long deliveredOrders;
    private Long cancelledOrders;
}
