package com.projeto.clinicaapi.ExceptionHandler.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;

@Component
public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        response.setStatus(status.value());
        response.setContentType("application/json;charset=UTF-8");

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, "Invalid token or not logged in");
        problemDetail.setTitle("Invalid token");
        problemDetail.setProperty("timestamp", Instant.now().toString());
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        String json = mapper.writeValueAsString(problemDetail);

        response.getWriter().write(json);
    }
}
