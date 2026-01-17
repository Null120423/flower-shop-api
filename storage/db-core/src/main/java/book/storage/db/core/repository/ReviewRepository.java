package book.storage.db.core.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import book.core.enums.ReviewStatus;
import book.storage.db.core.entity.ProductEntity;
import book.storage.db.core.entity.ReviewEntity;
import book.storage.db.core.entity.UserEntity;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, UUID> {
    Optional<ReviewEntity> findByUserAndProduct(UserEntity user, ProductEntity product);
    List<ReviewEntity> findByProduct(ProductEntity product);
    List<ReviewEntity> findByProductId(UUID productId);
    List<ReviewEntity> findByProductAndStatus(ProductEntity product, ReviewStatus status);
    List<ReviewEntity> findByUser(UserEntity user);
    List<ReviewEntity> findByStatus(ReviewStatus status);
    boolean existsByUserAndProduct(UserEntity user, ProductEntity product);
    
    @Query("SELECT AVG(r.rating) FROM ReviewEntity r WHERE r.product.id = :productId AND r.status = 'APPROVED'")
    Double getAverageRatingByProductId(UUID productId);
}
