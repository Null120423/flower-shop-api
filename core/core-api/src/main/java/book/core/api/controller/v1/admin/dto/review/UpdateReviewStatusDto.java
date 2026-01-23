package book.core.api.controller.v1.admin.dto.review;

import book.core.enums.ReviewStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReviewStatusDto {
    
    @NotNull(message = "Review status is required")
    private ReviewStatus status;
    
    private String adminNote;
}
