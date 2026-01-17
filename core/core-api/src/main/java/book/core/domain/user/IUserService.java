package book.core.domain.user;

import book.storage.db.core.entity.UserEntity;

import java.util.Optional;

public interface IUserService {
    Optional<UserEntity> findById(String userId);
}
