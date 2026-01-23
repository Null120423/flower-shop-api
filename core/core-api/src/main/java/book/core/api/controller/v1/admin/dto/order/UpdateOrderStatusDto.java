package book.core.api.controller.v1.admin.dto.order;

import book.core.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderStatusDto {
    
    @NotNull(message = "Order status is required")
    private OrderStatus status;
    
    private String note;
}
