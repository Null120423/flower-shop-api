package book.core.api.controller.v1.authentication.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private UserInfoDto userInfo;
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
}

