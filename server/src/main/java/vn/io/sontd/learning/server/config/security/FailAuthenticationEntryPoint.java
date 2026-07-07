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

public class FailAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        ResponseRoot root = new ResponseRoot();
        root.setCode(ResponseCode.ACCESS_DENIED);
        root.setMsg(Message.AUTH_ERROR);

        // SecurityContext をクリアする
        SecurityContext context = SecurityContextHolder.getContext();
        SecurityContextHolder.clearContext();
        context.setAuthentication(null);

        OutputStream responseStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(responseStream, root);
        responseStream.flush();
    }
}
