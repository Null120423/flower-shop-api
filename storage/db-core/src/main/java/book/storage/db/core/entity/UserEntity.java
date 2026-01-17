package book.storage.db.core.entity;

import book.core.enums.AuthProvider;
import jakarta.persistence.*;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = {"provider", "providerId"})
        }
)
public class UserEntity extends BaseEntity {
    @Column(length = 50)
    private String username;

    @Column(nullable = false)
    private String email;

    /**
     * Chỉ dùng cho login bằng username/password
     * Social login sẽ để null
     */
    @Column
    private String password;

    /**
     * PROVIDER: LOCAL, GOOGLE, FACEBOOK
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;

    /**
     * ID user từ Google / Facebook
     */
    @Column
    private String providerId;

    @Column(nullable = false)
    private boolean enabled = true;

    /* ===== Constructors ===== */

    public UserEntity() {}

    // Login LOCAL
    public UserEntity(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.provider = AuthProvider.LOCAL;
    }

    // Login SOCIAL
    public UserEntity(String email, AuthProvider provider, String providerId) {
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
    }

    /* ===== Getters ===== */

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
