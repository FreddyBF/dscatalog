package com.github.freddy.dscatalog.exception;


import com.github.freddy.dscatalog.dto.error.FieldMessage;
import com.github.freddy.dscatalog.dto.error.StandardError;
import com.github.freddy.dscatalog.dto.error.ValidationError;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // AUTENTICAÇÃO / AUTORIZAÇÃO

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<StandardError> handleAuthenticationException(
            AuthenticationException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Falha na autenticação",
                request,
                ex
        );
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<StandardError> handleJwtException(
            JwtException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Token inválido ou expirado",
                request,
                ex
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardError> handleAccessDeniedException(
            AccessDeniedException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(
                HttpStatus.FORBIDDEN,
                "Acesso negado",
                request,
                ex
        );
    }

    // =========================
    // EXCEÇÕES DE NEGÓCIO
    // =========================

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<StandardError> handleUnauthorizedException(
            UnauthorizedException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request, ex);
    }

    @ExceptionHandler({ResourceNotFoundException.class, UsernameNotFoundException.class})
    public ResponseEntity<StandardError> handleNotFoundException(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request, ex);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<StandardError> handleConflictException(
            ConflictException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request, ex);
    }


    // VALIDAÇÃO (@Valid)

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        List<FieldMessage> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::mapToFieldMessage)
                .toList();

        ValidationError error = new ValidationError(
                LocalDateTime.now(),
                status.value(),
                "Erro de validação",
                request.getRequestURI(),
                errors
        );

        log.warn("Validação falhou em {} - {} campos inválidos",
                request.getRequestURI(),
                errors.size()
        );

        return ResponseEntity.status(status).body(error);
    }



    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardError> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError error = new StandardError(
                LocalDateTime.now(),
                status.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno inesperado",
                request,
                ex
        );
    }


    private ResponseEntity<StandardError> buildErrorResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request,
            Exception ex
    ) {
        logException(status, request, ex);

        StandardError error = new StandardError(
                LocalDateTime.now(),
                status.value(),
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(error);
    }

    private void logException(HttpStatus status, HttpServletRequest request, Exception ex) {
        if (status.is5xxServerError()) {
            log.error("Erro interno em {}",
                    request.getRequestURI(),
                    ex
            );
        } else {
            log.warn("Erro {} em {} - {}",
                    status.value(),
                    request.getRequestURI(),
                    ex.getMessage()
            );
        }
    }

    private FieldMessage mapToFieldMessage(FieldError fieldError) {
        return new FieldMessage(
                fieldError.getField(),
                fieldError.getDefaultMessage()
        );
    }
}

