package book.core.api.controller.v1.authentication.response;

import java.util.Set;
import java.util.UUID;

import book.core.enums.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private UUID id;
    private String username;
    private String email;
    private boolean enabled;
    private AuthProvider provider;
    private Set<String> roles;
}
