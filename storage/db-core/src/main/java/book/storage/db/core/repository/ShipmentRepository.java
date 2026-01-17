package book.storage.db.core.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import book.core.enums.ShipmentStatus;
import book.storage.db.core.entity.OrderEntity;
import book.storage.db.core.entity.ShipmentEntity;

@Repository
public interface ShipmentRepository extends JpaRepository<ShipmentEntity, UUID> {
    Optional<ShipmentEntity> findByOrder(OrderEntity order);
    Optional<ShipmentEntity> findByOrderId(UUID orderId);
    Optional<ShipmentEntity> findByTrackingNumber(String trackingNumber);
    List<ShipmentEntity> findByStatus(ShipmentStatus status);
}
