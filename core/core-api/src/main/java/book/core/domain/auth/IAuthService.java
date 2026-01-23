package book.core.domain.auth;


import book.core.api.controller.v1.authentication.request.LoginRequestDto;
import book.core.api.controller.v1.authentication.request.RegisterRequestDto;
import book.core.api.controller.v1.authentication.response.LoginResponseDto;

import java.util.Optional;

public interface IAuthService {
    Optional<LoginResponseDto> login(LoginRequestDto data);
    Optional<String> register(RegisterRequestDto data);
}
