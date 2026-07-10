package vn.io.sontd.learning.server.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import tools.jackson.databind.ObjectMapper;
import vn.io.sontd.learning.server.constant.Message;
import vn.io.sontd.learning.server.constant.ResponseCode;
import vn.io.sontd.learning.server.response.ResponseRoot;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Handles authorization failures (authenticated but not permitted).
 * Instead of letting Spring Security return its default 403 error page,
 * this writes the standard {@link ResponseRoot} JSON envelope with HTTP 200
 * so the client-side response parser can handle it uniformly.
 */
public class FailAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * Writes a JSON {@link ResponseRoot} body describing the access-denied error.
     *
     * @param request               the request that resulted in an access-denied failure
     * @param response              the response to write the error body to
     * @param accessDeniedException the exception that triggered this handler
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);

        ResponseRoot root = new ResponseRoot();
        root.setCode(ResponseCode.ACCESS_DENIED);
        root.setMsg(Message.ACCESS_DENIED);

        OutputStream responseStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(responseStream, root);
        responseStream.flush();
    }
}
