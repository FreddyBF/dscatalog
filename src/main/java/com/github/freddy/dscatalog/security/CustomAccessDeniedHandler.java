package com.github.freddy.dscatalog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.freddy.dscatalog.dto.error.StandardError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final String FORBIDDEN_MESSAGE =
            "Acesso negado: você não possui privilégios suficientes";

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException ex)
            throws IOException, ServletException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        StandardError error = new StandardError(
                LocalDateTime.now(),
                HttpServletResponse.SC_FORBIDDEN,
                FORBIDDEN_MESSAGE,
                request.getRequestURI()
        );

        objectMapper.writeValue(response.getWriter(), error);
    }
}
