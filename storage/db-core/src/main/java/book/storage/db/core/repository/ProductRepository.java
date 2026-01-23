package book.storage.db.core.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import book.core.enums.ProductType;
import book.storage.db.core.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    Optional<ProductEntity> findBySku(String sku);
    List<ProductEntity> findByType(ProductType type);
    boolean existsBySku(String sku);
    
    @Query("SELECT p FROM ProductEntity p JOIN p.categories c WHERE c.id = :categoryId")
    List<ProductEntity> findByCategoryId(UUID categoryId);
    
    Page<ProductEntity> findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(
            String name, String sku, Pageable pageable);
}
