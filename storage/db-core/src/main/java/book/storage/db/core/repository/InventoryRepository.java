package book.storage.db.core.repository;

import book.storage.db.core.entity.InventoryEntity;
import book.storage.db.core.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, UUID> {
    Optional<InventoryEntity> findByProduct(ProductEntity product);
    Optional<InventoryEntity> findByProductId(UUID productId);
    
    @Query("SELECT i FROM InventoryEntity i WHERE i.stockQuantity <= i.minStockLevel")
    List<InventoryEntity> findLowStockProducts();
}
