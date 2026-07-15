package vn.io.sontd.learning.server.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.io.sontd.learning.server.config.security.UserDetailsImpl;

import java.util.Optional;

/**
 * Base class for {@code service/impl} classes, giving them access to the current
 * request's authenticated user without a controller having to resolve it and pass
 * it down as a parameter.
 */
public abstract class BaseService {

    /**
     * Returns the current request's authenticated user, if any.
     * Empty when the caller is anonymous (no/invalid bearer token) — including on
     * a permit-all endpoint (see {@link vn.io.sontd.learning.server.constant.Constant#INTERNAL_PERMIT_ALL})
     * accessed without a token, which is not an error there.
     *
     * @return the authenticated user, or empty if the current request is anonymous
     */
    protected Optional<UserDetailsImpl> getAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            return Optional.empty();
        }
        return Optional.of(userDetails);
    }
}
