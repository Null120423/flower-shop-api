package book.storage.db.core.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import book.core.enums.PaymentStatus;
import book.storage.db.core.entity.OrderEntity;
import book.storage.db.core.entity.PaymentEntity;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> {
    Optional<PaymentEntity> findByOrder(OrderEntity order);
    Optional<PaymentEntity> findByOrderId(UUID orderId);
    Optional<PaymentEntity> findByTransactionId(String transactionId);
    List<PaymentEntity> findByStatus(PaymentStatus status);
}
