package vn.io.sontd.learning.server.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Stores uploaded images on the local filesystem and exposes where they live,
 * so they can also be served back as static resources (see {@code WebConfig}).
 */
public interface ImageStorageService {

    /**
     * Stores an uploaded image under a generated, collision-free filename ({@code {uuid}.{extension}}).
     *
     * @param file the uploaded image
     * @return the generated bare filename (e.g. {@code ab12cd34.png}), exactly as persisted in
     *         {@code study_cards.img_url}; convert via {@link #toPublicUrl} before exposing it to a client
     * @throws vn.io.sontd.learning.server.exception.BusinessException if the file is empty or can't be written
     */
    String store(MultipartFile file);

    /**
     * Converts a stored image filename (as persisted in the DB, e.g. {@code ab12cd34.png}) into the
     * public URL clients should call to fetch it (e.g. {@code /api/v1/imgs/ab12cd34.png}). A value
     * that already looks like a URL/path (e.g. an external URL, or {@code null}/blank) is returned
     * unchanged, so non-managed images pass through untouched.
     *
     * @param storedFilename the bare filename as persisted in the DB
     * @return the corresponding public URL, or {@code storedFilename} unchanged if not a bare filename
     */
    String toPublicUrl(String storedFilename);

    /**
     * Inverse of {@link #toPublicUrl}: normalizes a public image URL back to the bare filename
     * before persisting, so the DB always holds just the filename regardless of what the client
     * echoes back (e.g. an unchanged image on a study set update). A value that isn't under the
     * public URL prefix (e.g. an external URL, or {@code null}/blank) is returned unchanged.
     *
     * @param publicUrl the URL as received from a client (typically a value earlier returned by {@link #toPublicUrl})
     * @return the corresponding bare filename, or {@code publicUrl} unchanged if not a managed URL
     */
    String toStoredPath(String publicUrl);

    /**
     * Returns the absolute directory where images are stored, resolved from the
     * {@code thesis.app.image.base-dir} property (relative values are resolved
     * against the directory containing the running jar).
     *
     * @return the absolute storage directory path
     */
    String getStorageLocation();
}
