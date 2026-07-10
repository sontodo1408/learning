package vn.io.sontd.learning.server.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import tools.jackson.databind.ObjectMapper;
import vn.io.sontd.learning.server.constant.Message;
import vn.io.sontd.learning.server.constant.ResponseCode;
import vn.io.sontd.learning.server.response.ResponseRoot;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Handles authentication failures (missing/invalid/expired token, bad credentials, etc.).
 * Instead of letting Spring Security return its default 401 error page,
 * this writes the standard {@link ResponseRoot} JSON envelope with HTTP 200
 * so the client-side response parser can handle it uniformly.
 */
public class FailAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Clears the (partial/invalid) security context and writes a JSON
     * {@link ResponseRoot} body describing the authentication error.
     *
     * @param request       the request that resulted in an authentication failure
     * @param response      the response to write the error body to
     * @param authException the exception that triggered this entry point
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        ResponseRoot root = new ResponseRoot();
        root.setCode(ResponseCode.ACCESS_DENIED);
        root.setMsg(Message.AUTH_ERROR);

        // Clear the security context so no partial/invalid authentication lingers
        SecurityContext context = SecurityContextHolder.getContext();
        SecurityContextHolder.clearContext();
        context.setAuthentication(null);

        OutputStream responseStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(responseStream, root);
        responseStream.flush();
    }
}
