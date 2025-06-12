package ru.itis.semworkapp.handler.ajax;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class AjaxAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        String errorMessage = "Неверное имя пользователя или пароль";

        if (ajax) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"" + errorMessage + "\"}");
        } else {
            response.sendRedirect("/login?error=true&message=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8));
        }
    }

}
