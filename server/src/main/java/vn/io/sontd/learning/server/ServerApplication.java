package vn.io.sontd.learning.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Application bootstrap class.
 * {@code @EnableJpaAuditing} activates automatic population of
 * {@link vn.io.sontd.learning.server.entity.BaseEntity#getCreatedAt()} and
 * {@link vn.io.sontd.learning.server.entity.BaseEntity#getUpdatedAt()} on persist/update.
 */
@SpringBootApplication
@EnableJpaAuditing
public class ServerApplication {

    /**
     * Starts the Spring Boot application context.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
