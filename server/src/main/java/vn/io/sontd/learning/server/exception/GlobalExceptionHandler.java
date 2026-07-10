package vn.io.sontd.learning.server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import vn.io.sontd.learning.server.constant.Message;
import vn.io.sontd.learning.server.constant.ResponseCode;
import vn.io.sontd.learning.server.response.ResponseRoot;

/**
 * Global exception handler ({@code @RestControllerAdvice}) applied to every controller.
 * Catches any exception that escapes a controller/service method so the API
 * never leaks a raw stack trace/HTML error page to the client.
 * <p>
 * {@link BadCredentialsException} and {@link BusinessException} carry their
 * own client-facing message; every other exception falls back to the generic
 * {@link Message#SYS_ERROR} catch-all. Add more specific {@code @ExceptionHandler}
 * methods as distinct error cases need their own code/message.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles invalid-credentials failures (e.g. wrong username/password on login).
     *
     * @param ex the thrown credentials exception
     * @return a {@link ResponseRoot} with {@link ResponseCode#BUSINESS_ERROR} and the exception's own message
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseRoot handleBadCredentialsException(BadCredentialsException ex) {
        log.error(ex.getMessage(), ex);

        return ResponseRoot.builder() //
                .code(ResponseCode.BUSINESS_ERROR) //
                .msg(ex.getMessage()) //
                .build();
    }

    /**
     * Handles expected business-rule violations raised deliberately by service/business logic.
     *
     * @param ex the thrown business exception
     * @return a {@link ResponseRoot} with {@link ResponseCode#BUSINESS_ERROR} and the exception's own message
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseRoot handleBusinessException(BusinessException ex) {
        log.error(ex.getMessage(), ex);

        return ResponseRoot.builder() //
                .code(ResponseCode.BUSINESS_ERROR) //
                .msg(ex.getMessage()) //
                .build();
    }

    /**
     * Handles requests to an API path that has no matching/declared controller mapping
     * (e.g. {@link org.springframework.web.servlet.resource.NoResourceFoundException}, which
     * extends this class and is thrown for unmapped paths since Spring 6).
     *
     * @param ex the thrown no-handler-found exception
     * @return a {@link ResponseRoot} with {@link ResponseCode#BUSINESS_ERROR} and {@link Message#SYS_ERROR}
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseRoot handleNoHandlerFoundException(NoHandlerFoundException ex) {
        log.error(ex.getMessage(), ex);

        return ResponseRoot.builder() //
                .code(ResponseCode.BUSINESS_ERROR) //
                .msg(Message.SYS_ERROR) //
                .build();
    }

    /**
     * Handles any other uncaught exception.
     *
     * @param ex the exception that propagated out of the controller/service layer
     * @return a {@link ResponseRoot} with {@link ResponseCode#BUSINESS_ERROR} and {@link Message#SYS_ERROR}
     */
    @ExceptionHandler(Exception.class)
    public ResponseRoot handleException(Exception ex) {
        log.error(ex.getMessage(), ex);

        return ResponseRoot.builder() //
                .code(ResponseCode.BUSINESS_ERROR) //
                .msg(Message.SYS_ERROR) //
                .build();
    }
}
