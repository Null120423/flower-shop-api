package book.core.api.controller.v1.user.authentication.response;


import book.storage.db.core.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private UserEntity userInfo;
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
}

