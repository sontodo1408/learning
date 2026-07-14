package vn.io.sontd.learning.server.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vn.io.sontd.learning.server.constant.Constant;
import vn.io.sontd.learning.server.service.ImageStorageService;

/**
 * Web MVC configuration. Serves uploaded images (which live on the filesystem
 * outside the classpath) at the public {@link Constant#IMAGE_URL_PREFIX} URL,
 * so the URLs stored on study cards resolve to a viewable image.
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final ImageStorageService imageStorageService;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Spring's resource loader needs a "file:" URL ending in a slash; normalize Windows separators too.
        String location = "file:" + imageStorageService.getStorageLocation().replace('\\', '/');
        if (!location.endsWith("/")) {
            location += "/";
        }
        registry.addResourceHandler(Constant.IMAGE_URL_PREFIX + "/**")
                .addResourceLocations(location);
    }
}
