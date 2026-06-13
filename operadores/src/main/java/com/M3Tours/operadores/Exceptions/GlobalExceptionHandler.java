package com.M3Tours.operadores.Exceptions;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import static net.logstash.logback.argument.StructuredArguments.kv;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND; // Código HTTP 404
        // Registramos la advertencia estructurada para Grafana Loki
        log.warn("Ruta o recurso estático no encontrado");
        // Construimos la respuesta homogénea para el cliente
        ErrorResponse error = new ErrorResponse();
        error.setMensaje("Ruta no encontrada");
        error.setDetalle("El endpoint '" + request.getRequestURI() + "' no existe en este servidor o el recurso estático no fue encontrado.");
        error.setStatus(status.value()); // 404
        error.setTimeStamp(LocalDateTime.now());
        return ResponseEntity.status(status).body(error);
    }
}
