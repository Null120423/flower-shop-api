package book.storage.db.core.repository;

import book.storage.db.core.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {
    Optional<CategoryEntity> findByName(String name);
    Optional<CategoryEntity> findBySlug(String slug);
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
}
