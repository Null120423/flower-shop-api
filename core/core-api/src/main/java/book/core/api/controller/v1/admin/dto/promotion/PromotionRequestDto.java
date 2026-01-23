package book.core.api.controller.v1.admin.dto.promotion;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import book.core.enums.PromotionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromotionRequestDto {
    
    @NotBlank(message = "Promotion code is required")
    @Size(max = 64, message = "Promotion code must not exceed 64 characters")
    private String code;
    
    @NotBlank(message = "Promotion name is required")
    @Size(max = 150, message = "Promotion name must not exceed 150 characters")
    private String name;
    
    private String description;
    
    @NotNull(message = "Promotion type is required")
    private PromotionType type;
    
    @NotNull(message = "Promotion value is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Promotion value must be greater than 0")
    private BigDecimal value;
    
    private LocalDateTime startAt;
    
    private LocalDateTime endAt;
    
    private Boolean active;
    
    @Min(value = 1, message = "Usage limit must be at least 1")
    private Integer usageLimit;
    
    private Set<String> productIds;
}
