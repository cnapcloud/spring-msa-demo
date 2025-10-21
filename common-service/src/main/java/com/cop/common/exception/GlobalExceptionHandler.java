package com.cop.common.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingHeaderException.class)
    public ResponseEntity<Object> handleMissingHeaderException(MissingHeaderException ex, WebRequest request) {
        Map<String, Object> response = buildResponse(ex, request.getDescription(false), HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {

        Map<String, Object> response = buildResponse(ex, request.getDescription(false), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Object> handleAuthorizationDeniedException(AuthorizationDeniedException ex,
            WebRequest request) {
        Map<String, Object> response = buildResponse(ex, request.getDescription(false), HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        Map<String, Object> response = buildResponse(ex, request.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, Object> buildResponse(Exception ex, String path, HttpStatus status) {
        log.error("An error occurred while performing the task", ex);

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", ex.getMessage());
        response.put("path", path);

        return response;
    }
}
