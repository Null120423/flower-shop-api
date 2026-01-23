package book.core.api.controller.v1.admin.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesSummary {
    private BigDecimal todaySales;
    private BigDecimal thisWeekSales;
    private BigDecimal thisMonthSales;
    private BigDecimal totalSales;
}
