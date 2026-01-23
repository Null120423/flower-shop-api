package book.core.api.controller.v1.admin.dto.promotion;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import book.core.enums.PromotionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromotionResponseDto {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private PromotionType type;
    private BigDecimal value;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Boolean active;
    private Integer usageLimit;
    private Integer usageCount;
    private Integer applicableProductCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
