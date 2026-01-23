package book.storage.db.core.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import book.core.enums.AuthProvider;
import book.storage.db.core.entity.RoleEntity;
import book.storage.db.core.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);
@Query("""
SELECT r FROM UserEntity u
JOIN u.roles r
WHERE u.id = :id
""")
List<RoleEntity> findRolesByUserId(@Param("id") UUID userId);


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
