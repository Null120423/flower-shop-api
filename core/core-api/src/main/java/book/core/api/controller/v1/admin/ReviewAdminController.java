package book.core.api.controller.v1.admin;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import book.core.api.controller.v1.admin.dto.review.ReviewResponseDto;
import book.core.api.controller.v1.admin.dto.review.UpdateReviewStatusDto;
import book.core.enums.ReviewStatus;
import book.core.support.response.ApiResponse;
import book.storage.db.core.entity.ReviewEntity;
import book.storage.db.core.repository.ReviewRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Review Management", description = "API for managing customer reviews and moderation")
public class ReviewAdminController extends AdminV1Controller {

    private final ReviewRepository reviewRepository;

    public ReviewAdminController(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // Get all reviews with filtering
    @GetMapping("/reviews")
    @Operation(summary = "Get all reviews", description = "Retrieve all reviews with optional status filtering")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reviews retrieved successfully")
    })
    public ResponseEntity<ApiResponse<Page<ReviewResponseDto>>> getAllReviews(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Filter by review status") @RequestParam(required = false) ReviewStatus status
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        Page<ReviewEntity> reviews;
        if (status != null) {
            List<ReviewEntity> filtered = reviewRepository.findByStatus(status);
            reviews = new PageImpl<>(filtered, pageable, filtered.size());
        } else {
            reviews = reviewRepository.findAll(pageable);
        }
        
        Page<ReviewResponseDto> response = reviews.map(this::toResponseDto);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    // Get review by ID
    @GetMapping("/reviews/{id}")
    @Operation(summary = "Get review by ID", description = "Retrieve a specific review by ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Review retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ResponseEntity<ApiResponse<ReviewResponseDto>> getReviewById(
            @Parameter(description = "Review ID") @PathVariable UUID id
    ) {
        return reviewRepository.findById(id)
                .map(review -> ResponseEntity.ok(ApiResponse.ok(toResponseDto(review))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail(null, "Review not found")));
    }

    // Get reviews by product
    @GetMapping("/reviews/product/{productId}")
    @Operation(summary = "Get product reviews", description = "Retrieve all reviews for a specific product")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product reviews retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> getReviewsByProduct(
            @Parameter(description = "Product ID") @PathVariable UUID productId
    ) {
        List<ReviewEntity> reviews = reviewRepository.findByProductId(productId);
        List<ReviewResponseDto> response = reviews.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    // Get pending reviews (for moderation)
    @GetMapping("/reviews/pending")
    @Operation(summary = "Get pending reviews", description = "Retrieve reviews pending moderation approval")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pending reviews retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> getPendingReviews() {
        List<ReviewEntity> pending = reviewRepository.findByStatus(ReviewStatus.PENDING);
        
        List<ReviewResponseDto> response = pending.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    // Update review status (approve/reject)
    @PatchMapping("/reviews/{id}/status")
    @Operation(summary = "Update review status", description = "Change the status of a review")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Review status updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ResponseEntity<ApiResponse<ReviewResponseDto>> updateReviewStatus(
            @Parameter(description = "Review ID") @PathVariable UUID id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "New review status") 
            @Valid @RequestBody UpdateReviewStatusDto request
    ) {
        return reviewRepository.findById(id)
                .map(review -> {
                    review.setStatus(request.getStatus());
                    ReviewEntity updated = reviewRepository.save(review);
                    return ResponseEntity.ok(ApiResponse.ok(toResponseDto(updated)));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail(null, "Review not found")));
    }

    // Approve review
    @PatchMapping("/reviews/{id}/approve")
    @Operation(summary = "Approve review", description = "Quick approve a pending review")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Review status updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ResponseEntity<ApiResponse<ReviewResponseDto>> approveReview(
            @Parameter(description = "Review ID") @PathVariable UUID id
    ) {
        return reviewRepository.findById(id)
                .map(review -> {
                    review.setStatus(ReviewStatus.APPROVED);
                    ReviewEntity updated = reviewRepository.save(review);
                    return ResponseEntity.ok(ApiResponse.ok(toResponseDto(updated)));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail(null, "Review not found")));
    }

    // Reject review
    @PatchMapping("/reviews/{id}/reject")
    @Operation(summary = "Reject review", description = "Quick reject a pending review")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Review rejected successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ResponseEntity<ApiResponse<ReviewResponseDto>> rejectReview(
            @Parameter(description = "Review ID") @PathVariable UUID id
    ) {
        return reviewRepository.findById(id)
                .map(review -> {
                    review.setStatus(ReviewStatus.REJECTED);
                    ReviewEntity updated = reviewRepository.save(review);
                    return ResponseEntity.ok(ApiResponse.ok(toResponseDto(updated)));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail(null, "Review not found")));
    }

    // Delete review (soft delete)
    @DeleteMapping("/reviews/{id}")
    @Operation(summary = "Delete review", description = "Delete a review (soft delete)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Review deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @Parameter(description = "Review ID") @PathVariable UUID id
    ) {
        return reviewRepository.findById(id)
                .map(review -> {
                    reviewRepository.delete(review);
                    return ResponseEntity.ok(ApiResponse.ok());
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail(null, "Review not found")));
    }

    private ReviewResponseDto toResponseDto(ReviewEntity review) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setId(review.getId());
        dto.setProductId(review.getProduct().getId());
        dto.setProductName(review.getProduct().getName());
        dto.setUserId(review.getUser().getId());
        dto.setUserEmail(review.getUser().getEmail());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setStatus(review.getStatus());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());
        return dto;
    }
}
