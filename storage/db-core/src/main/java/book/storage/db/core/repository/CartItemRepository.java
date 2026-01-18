package book.storage.db.core.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import book.storage.db.core.entity.CartEntity;
import book.storage.db.core.entity.CartItemEntity;
import book.storage.db.core.entity.ProductEntity;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, UUID> {
    List<CartItemEntity> findByCart(CartEntity cart);
    List<CartItemEntity> findByCartId(UUID cartId);
    Optional<CartItemEntity> findByCartAndProduct(CartEntity cart, ProductEntity product);
    void deleteByCart(CartEntity cart);
    void deleteByCartId(UUID cartId);
}
