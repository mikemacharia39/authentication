package com.mikehenry.authentication.domain.exception;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import com.mikehenry.authentication.api.dto.Problem;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Problem> handleRequestNotValidException(MethodArgumentNotValidException e) {

        List<String> errors = new ArrayList<>();
        e.getBindingResult()
                .getFieldErrors().forEach(error -> errors.add(error.getField() + ": " + error.getDefaultMessage()));
        e.getBindingResult()
                .getGlobalErrors() //Global errors are not associated with a specific field but are related to the entire object being validated.
                .forEach(error -> errors.add(error.getObjectName() + ": " + error.getDefaultMessage()));

        String message = "Validation of request failed: %s".formatted(String.join(", ", errors));
        return ResponseEntity.status(BAD_REQUEST).body(new Problem(BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Problem> handleBadCredentialsException() {
        return ResponseEntity.status(UNAUTHORIZED)
                .body(new Problem(UNAUTHORIZED.value(), "Invalid username or password"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Problem> handleAccessDeniedException() {
        return ResponseEntity.status(FORBIDDEN)
                .body(new Problem(FORBIDDEN.value(), "You do not have permission to access this resource"));
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<Problem> handleSignatureException() {
        return ResponseEntity.status(UNAUTHORIZED)
                .body(new Problem(UNAUTHORIZED.value(), "JWT signature is invalid"));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Problem> handleExpiredToken() {
        return ResponseEntity.status(UNAUTHORIZED)
                .body(new Problem(UNAUTHORIZED.value(), "Token has expired"));
    }
}
