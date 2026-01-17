package book.core.domain.user.impl;

import book.core.domain.user.IUserService;
import book.storage.db.core.entity.UserEntity;
import book.storage.db.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepo;
    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
    @Override
    public Optional<UserEntity> findById(String userId) {
        return userRepo.findById(userId);
    }
}
