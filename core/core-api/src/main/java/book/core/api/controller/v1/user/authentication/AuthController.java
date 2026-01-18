package book.core.api.controller.v1.user.authentication;

import book.core.api.controller.v1.user.UserV1Controller;
import book.core.api.controller.v1.user.authentication.request.LoginRequestDto;
import book.core.api.controller.v1.user.authentication.request.RegisterRequestDto;
import book.core.api.controller.v1.user.authentication.response.LoginResponseDto;
import book.core.domain.auth.impl.AuthService;
import book.core.support.error.ErrorType;
import book.core.support.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController extends UserV1Controller {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(
            @RequestBody RegisterRequestDto data
    ) {
        return authService.register(data)
                .map(res ->
                        ResponseEntity.ok(ApiResponse.ok(res))
                )
                .orElseGet(() ->
                        ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(ApiResponse.fail(
                                        ErrorType.DEFAULT_ERROR,
                                        "Failed register user!"
                                ))
                );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(
            @RequestBody LoginRequestDto data
    ) {
        return authService.login(data)
                .map(result ->
                        ResponseEntity.ok(ApiResponse.ok(result))
                )
                .orElseGet(() ->
                        ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(ApiResponse.fail(
                                        ErrorType.UNAUTHORIZED,
                                        "Invalid email or password"
                                ))
                );
    }
}
