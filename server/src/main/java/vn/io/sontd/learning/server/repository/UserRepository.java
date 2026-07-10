package vn.io.sontd.learning.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.io.sontd.learning.server.entity.UserEntity;

import java.util.Optional;

/**
 * Data access for {@link UserEntity}.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Looks up a user by their unique username.
     *
     * @param username the username to search for
     * @return the matching user, or empty if none exists
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * Looks up a user by their email address (used to match/link a Google account
     * to an existing user, since Google doesn't share the app's username).
     *
     * @param email the email to search for
     * @return the matching user, or empty if none exists
     */
    Optional<UserEntity> findByEmail(String email);
}
