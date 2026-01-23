package book.storage.db.core.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import book.storage.db.core.entity.PromotionEntity;

@Repository
public interface PromotionRepository extends JpaRepository<PromotionEntity, UUID> {
    Optional<PromotionEntity> findByCode(String code);
    List<PromotionEntity> findByActive(Boolean active);
    Page<PromotionEntity> findByActive(Boolean active, Pageable pageable);
    
    List<PromotionEntity> findByActiveAndStartAtBeforeAndEndAtAfter(
            Boolean active, 
            LocalDateTime now1, 
            LocalDateTime now2
    );
    
    boolean existsByCode(String code);
}
