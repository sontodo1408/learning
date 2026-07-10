package vn.io.sontd.learning.server.exception;

/**
 * Generic exception for expected business-rule violations (e.g. invalid
 * state, failed validation, not-found lookups). Thrown from service/business
 * logic and translated by {@link GlobalExceptionHandler} into a business-error
 * response (code 400) carrying this exception's message.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
