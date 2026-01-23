package book.core.api.controller.v1.admin.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private Integer productCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
