package book.core.domain.auth;


import book.core.api.controller.v1.user.authentication.request.LoginRequestDto;
import book.core.api.controller.v1.user.authentication.request.RegisterRequestDto;
import book.core.api.controller.v1.user.authentication.response.LoginResponseDto;
import book.storage.db.core.entity.UserEntity;

import java.util.Optional;

public interface IAuthService {
    Optional<LoginResponseDto> login(LoginRequestDto data);
    Optional<String> register(RegisterRequestDto data);
}
