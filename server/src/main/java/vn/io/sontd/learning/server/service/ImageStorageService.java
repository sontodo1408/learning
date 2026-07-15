package vn.io.sontd.learning.server.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Stores uploaded images on the local filesystem and exposes where they live,
 * so they can also be served back as static resources (see {@code WebConfig}).
 */
public interface ImageStorageService {

    /**
     * Stores an uploaded image under {@code subDirectory}, using a generated, collision-free
     * filename ({@code {uuid}.{extension}}).
     *
     * @param file the uploaded image
     * @param subDirectory the subdirectory to store the image under, relative to the configured
     *                     base directory (e.g. {@code "study"})
     * @return the generated path, relative to the base directory (e.g. {@code study/ab12cd34.png}),
     *         exactly as persisted in {@code study_cards.img_url} and returned as-is to clients;
     *         the client is responsible for building the full URL (e.g. by prepending {@code /api/v1/imgs/})
     * @throws vn.io.sontd.learning.server.exception.BusinessException if the file is empty or can't be written
     */
    String store(MultipartFile file, String subDirectory);

    /**
     * Normalizes a value echoed back by a client into the bare filename to persist, so the DB
     * always holds just the filename regardless of what the client sends (e.g. an unchanged image
     * on a study set update, possibly still carrying a full URL from before the client stopped
     * receiving one). A value that isn't under the public URL prefix (e.g. an external URL, an
     * already-bare filename, or {@code null}/blank) is returned unchanged.
     *
     * @param clientValue the {@code imgUrl} value as received from a client
     * @return the corresponding bare filename, or {@code clientValue} unchanged if not a prefixed URL
     */
    String toStoredPath(String clientValue);

    /**
     * Returns the absolute directory where images are stored, resolved from the
     * {@code thesis.app.image.base-dir} property (relative values are resolved
     * against the directory containing the running jar).
     *
     * @return the absolute storage directory path
     */
    String getStorageLocation();
}
