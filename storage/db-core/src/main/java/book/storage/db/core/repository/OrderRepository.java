package book.storage.db.core.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import book.core.enums.OrderStatus;
import book.storage.db.core.entity.OrderEntity;
import book.storage.db.core.entity.UserEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    List<OrderEntity> findByUser(UserEntity user);
    List<OrderEntity> findByUserId(UUID userId);
    List<OrderEntity> findByUserAndStatus(UserEntity user, OrderStatus status);
    List<OrderEntity> findByStatus(OrderStatus status);
    
    @Query("SELECT o FROM OrderEntity o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    List<OrderEntity> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
