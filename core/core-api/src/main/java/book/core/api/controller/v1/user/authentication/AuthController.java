package book.core.api.controller.v1.user.authentication;

import book.authentication.service.JwtService;
import book.core.api.controller.v1.user.UserV1Controller;
import book.core.api.controller.v1.user.authentication.request.LoginRequestDto;
import book.core.api.controller.v1.user.authentication.request.RegisterRequestDto;
import book.core.api.controller.v1.user.authentication.response.LoginResponseDto;
import book.core.domain.auth.impl.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController extends UserV1Controller {

    private final AuthService authService;

    public AuthController(
            AuthService authService
    ) {
        this.authService = authService;
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDto data) {
        return authService.register(data)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to register user"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto data) {
        return authService.login(data)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}

