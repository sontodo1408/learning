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

public class FailAccessDeniedHandler implements AccessDeniedHandler {
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
