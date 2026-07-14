package vn.io.sontd.learning.server.service.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.io.sontd.learning.server.constant.Constant;
import vn.io.sontd.learning.server.constant.Message;
import vn.io.sontd.learning.server.exception.BusinessException;
import vn.io.sontd.learning.server.service.ImageStorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Filesystem-backed {@link ImageStorageService}.
 */
@Service
public class ImageStorageServiceImpl implements ImageStorageService {
    /** Only a short, alphanumeric extension (kept from the original filename) is trusted. */
    private static final String SAFE_EXTENSION_PATTERN = "\\.[a-z0-9]{1,10}";

    @Value("${thesis.app.image.base-dir}")
    private String configuredBaseDir;

    /** Absolute, normalized directory the images are written to; computed once at startup. */
    private Path storageDirectory;

    /**
     * Resolves the configured base directory and makes sure it exists.
     * An absolute path is used verbatim; a relative one is resolved against the
     * directory containing the running jar — so a production value of {@code imgs}
     * lands in {@code {jar dir}/imgs}. Fails fast if the directory can't be created.
     */
    @PostConstruct
    void init() {
        Path base = Paths.get(configuredBaseDir);
        if (!base.isAbsolute()) {
            Path jarDir = new ApplicationHome(ImageStorageServiceImpl.class).getDir().toPath();
            base = jarDir.resolve(configuredBaseDir);
        }
        this.storageDirectory = base.toAbsolutePath().normalize();
        try {
            Files.createDirectories(storageDirectory);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot create image storage directory: " + storageDirectory, e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(Message.IMAGE_UPLOAD_FAIL);
        }

        // Generated name avoids collisions and path-traversal from the client-supplied filename.
        String filename = UUID.randomUUID().toString().replace("-", "") + extractExtension(file.getOriginalFilename());
        try {
            file.transferTo(storageDirectory.resolve(filename));
        } catch (IOException e) {
            throw new BusinessException(Message.IMAGE_UPLOAD_FAIL, e);
        }

        return filename;
    }

    @Override
    public String toPublicUrl(String storedFilename) {
        // Anything that already looks like a path/URL (external image, or already-converted
        // value) is left untouched — only a bare filename gets prefixed.
        if (storedFilename == null || storedFilename.isBlank() || storedFilename.contains("/")) {
            return storedFilename;
        }
        return Constant.IMAGE_URL_PREFIX + "/" + storedFilename;
    }

    @Override
    public String toStoredPath(String publicUrl) {
        String prefix = Constant.IMAGE_URL_PREFIX + "/";
        if (publicUrl == null || !publicUrl.startsWith(prefix)) {
            return publicUrl;
        }
        return publicUrl.substring(prefix.length());
    }

    @Override
    public String getStorageLocation() {
        return storageDirectory.toString();
    }

    /**
     * Extracts a safe file extension (including the dot) from the original filename,
     * or an empty string if it's missing or looks unsafe.
     */
    private String extractExtension(String originalFilename) {
        if (originalFilename == null) {
            return "";
        }
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex < 0) {
            return "";
        }
        String extension = originalFilename.substring(dotIndex).toLowerCase();
        return extension.matches(SAFE_EXTENSION_PATTERN) ? extension : "";
    }
}
