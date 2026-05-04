package com.duoc.mascotasordenes.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manejador global que traduce excepciones del dominio y de Bean Validation a respuestas
 * JSON consistentes con timestamp, status, error, message y path.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNoEncontradoException.class)                                  // → 404
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(
            RecursoNoEncontradoException ex, WebRequest request) {
        log.warn("404 - {}", ex.getMessage());
        return construirRespuesta(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(ReglaNegocioException.class)                                         // → 400 (stock insuficiente, estado inválido, etc.)
    public ResponseEntity<Map<String, Object>> manejarReglaNegocio(
            ReglaNegocioException ex, WebRequest request) {
        log.warn("400 - Regla de negocio: {}", ex.getMessage());
        return construirRespuesta(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)                               // → 400 con detalle por campo (Bean Validation)
    public ResponseEntity<Map<String, Object>> manejarValidacion(
            MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                errores.put(fieldError.getField(), fieldError.getDefaultMessage()));
        log.warn("400 - Validación fallida: {}", errores);
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("status", HttpStatus.BAD_REQUEST.value());
        cuerpo.put("error", "Validación fallida");
        cuerpo.put("message", "La petición contiene datos inválidos");
        cuerpo.put("path", request.getDescription(false).replace("uri=", ""));
        cuerpo.put("errors", errores);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cuerpo);
    }

    @ExceptionHandler(IllegalArgumentException.class)                                      // → 400 (argumentos ilegales en código legado)
    public ResponseEntity<Map<String, Object>> manejarIlegalArgument(
            IllegalArgumentException ex, WebRequest request) {
        log.warn("400 - Argumento ilegal: {}", ex.getMessage());
        return construirRespuesta(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)                                                     // Red de seguridad → 500
    public ResponseEntity<Map<String, Object>> manejarGenerico(
            Exception ex, WebRequest request) {
        log.error("500 - Error inesperado", ex);
        return construirRespuesta(HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno en el servidor: " + ex.getMessage(),
                request);
    }

    /** Cuerpo JSON estándar usado por todos los handlers. */
    private ResponseEntity<Map<String, Object>> construirRespuesta(
            HttpStatus status, String mensaje, WebRequest request) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("status", status.value());
        cuerpo.put("error", status.getReasonPhrase());
        cuerpo.put("message", mensaje);
        cuerpo.put("path", request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(status).body(cuerpo);
    }
}
