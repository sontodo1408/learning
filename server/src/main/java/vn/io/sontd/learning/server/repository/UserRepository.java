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
}
