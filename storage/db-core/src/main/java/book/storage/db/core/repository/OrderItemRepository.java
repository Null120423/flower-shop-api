package book.storage.db.core.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import book.storage.db.core.entity.OrderEntity;
import book.storage.db.core.entity.OrderItemEntity;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, UUID> {
    List<OrderItemEntity> findByOrder(OrderEntity order);
    List<OrderItemEntity> findByOrderId(UUID orderId);
}
