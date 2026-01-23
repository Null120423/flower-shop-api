package book.core.domain.auth.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import book.authentication.service.JwtService;
import book.core.api.controller.v1.authentication.request.LoginRequestDto;
import book.core.api.controller.v1.authentication.request.RegisterRequestDto;
import book.core.api.controller.v1.authentication.response.LoginResponseDto;
import book.core.api.controller.v1.authentication.response.UserInfoDto;
import book.core.domain.auth.IAuthService;
import book.core.enums.AuthProvider;
import book.storage.db.core.entity.RoleEntity;
import book.storage.db.core.entity.UserEntity;
import book.storage.db.core.repository.UserRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AuthService implements IAuthService {
    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Optional<LoginResponseDto> login(LoginRequestDto data) {
        Optional<UserEntity> userOpt = userRepo.findByEmail(data.getEmail());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        UserEntity user = userOpt.get();
        if (!passwordEncoder.matches(data.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }
        // password đúng → tạo JWT
        user.setPassword("");
        String accessToken = jwtService.generateToken(user.getId().toString());
        String refreshToken = jwtService.genRefreshToken(user.getId().toString());

        List<RoleEntity> roles = userRepo.findRolesByUserId(user.getId());
        Set<String> roleNames = roles.stream()
            .map(role -> role.getName())
            .collect(Collectors.toSet());

        UserInfoDto userInfo = UserInfoDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .enabled(user.isEnabled())
            .provider(user.getProvider())
            .roles(roleNames)
            .build();

        LoginResponseDto response = LoginResponseDto.builder()
            .userInfo(userInfo)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(3600L)
                .build();

        return Optional.of(response);

    }

    @Override
    public Optional<String> register(RegisterRequestDto data) {
        // 1. Validate email
        if (data.getEmail() == null || data.getEmail().isEmpty()) {
            throw new RuntimeException("Email is empty");
        }

        // 2. Validate password
        if (data.getPassword() == null || data.getPassword().isEmpty()) {
            throw new RuntimeException("Password is empty");
        }

        // 3. Check if email already exists
        if (userRepo.existsByEmail(data.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        // 4. Encode password
        String encodedPassword = passwordEncoder.encode(data.getPassword());

        // 5. Create new user
        UserEntity newUser = new UserEntity();
        newUser.setEmail(data.getEmail());
        newUser.setPassword(encodedPassword);
        newUser.setProvider(AuthProvider.LOCAL);
        newUser.setEnabled(true);

        // 6. Save to database
        userRepo.save(newUser);

        return Optional.of("User registered successfully");
    }

}
