package vn.io.sontd.learning.server.config.security;

import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.io.sontd.learning.server.constant.enums.EUserStatus;
import vn.io.sontd.learning.server.entity.UserEntity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * Spring Security {@link UserDetails} view of a {@link UserEntity}.
 * Carries only the fields needed for authentication/authorization; it is
 * intentionally not a full user profile (see {@code LoginResponse} for that).
 */
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phoneNumber;
    private LocalDate birthday;
    private String role;
    private EUserStatus status;

    /**
     * Builds a {@link UserDetailsImpl} from a persisted {@link UserEntity}.
     *
     * @param entity the source user entity
     * @return the corresponding Spring Security user details
     */
    public static UserDetailsImpl build(UserEntity entity) {
        return new UserDetailsImpl(entity.getId(), entity.getUsername(), entity.getPassword(),
                entity.getFullName(), entity.getEmail(), entity.getPhoneNumber(),
                entity.getBirthday(), entity.getRole(), entity.getStatus());
    }

    /**
     * Grants a single authority derived from the user's role (e.g. {@code ROLE_ADMIN}).
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role));
    }

    /**
     * Returns the DB-encoded (hashed) password, used for credential checks.
     */
    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getRole() {
        return role;
    }

    public EUserStatus getStatus() {
        return status;
    }
}
