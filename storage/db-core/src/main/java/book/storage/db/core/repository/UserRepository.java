package book.storage.db.core.repository;

import book.core.enums.AuthProvider;
import book.storage.db.core.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByEmailAndProvider(
            String email,
            AuthProvider provider
    );

    Optional<UserEntity> findByProviderAndProviderId(
            AuthProvider provider,
            String providerId
    );

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
