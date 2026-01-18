package book.storage.db.core.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import book.core.enums.CartStatus;
import book.storage.db.core.entity.CartEntity;
import book.storage.db.core.entity.UserEntity;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, UUID> {
    Optional<CartEntity> findByUserAndStatus(UserEntity user, CartStatus status);
    Optional<CartEntity> findByUserIdAndStatus(UUID userId, CartStatus status);
    List<CartEntity> findByUser(UserEntity user);
    List<CartEntity> findByStatus(CartStatus status);
}
