package com.duoc.mascotasordenes.exception; // Paquete donde viven las excepciones y su manejador global

import org.slf4j.Logger; // Interfaz estándar de logging
import org.slf4j.LoggerFactory; // Fábrica para obtener un Logger vinculado a esta clase
import org.springframework.http.HttpStatus; // Enumeración de códigos de estado HTTP
import org.springframework.http.ResponseEntity; // Permite devolver respuestas HTTP con cuerpo y código de estado
import org.springframework.web.bind.MethodArgumentNotValidException; // Se lanza cuando @Valid falla en el cuerpo de una petición
import org.springframework.web.bind.annotation.ExceptionHandler; // Marca un método como manejador de un tipo de excepción
import org.springframework.web.bind.annotation.RestControllerAdvice; // Activa el manejo global de excepciones para todos los @RestController
import org.springframework.web.context.request.WebRequest; // Permite acceder a la ruta (URI) donde ocurrió el error

import java.time.LocalDateTime; // Marca de tiempo para incluir en la respuesta de error
import java.util.HashMap; // Mapa mutable para construir el cuerpo JSON de error
import java.util.LinkedHashMap; // Mapa que conserva el orden de inserción (mejor para leer la respuesta)
import java.util.Map; // Interfaz de mapa devuelta como cuerpo JSON

@RestControllerAdvice // Aplica a todos los controladores REST del microservicio: captura excepciones y devuelve JSON
public class GlobalExceptionHandler { // Convierte cualquier excepción lanzada en una respuesta HTTP coherente (IL6)

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class); // Logger para registrar cada error capturado

    @ExceptionHandler(RecursoNoEncontradoException.class) // Captura RecursoNoEncontradoException
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado( // Retorna 404 Not Found con cuerpo descriptivo
            RecursoNoEncontradoException ex, WebRequest request) { // Recibe la excepción y el request para extraer la ruta
        log.warn("404 - {}", ex.getMessage()); // Log WARN: recurso no encontrado
        return construirRespuesta(HttpStatus.NOT_FOUND, ex.getMessage(), request); // Construye la respuesta 404
    }

    @ExceptionHandler(ReglaNegocioException.class) // Captura ReglaNegocioException (stock insuficiente, estado inválido, etc.)
    public ResponseEntity<Map<String, Object>> manejarReglaNegocio( // Retorna 400 Bad Request con cuerpo descriptivo
            ReglaNegocioException ex, WebRequest request) { // Recibe la excepción y el request
        log.warn("400 - Regla de negocio: {}", ex.getMessage()); // Log WARN
        return construirRespuesta(HttpStatus.BAD_REQUEST, ex.getMessage(), request); // Construye la respuesta 400
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // Captura errores de validación de Bean Validation (@Valid)
    public ResponseEntity<Map<String, Object>> manejarValidacion( // Retorna 400 Bad Request con todos los campos inválidos
            MethodArgumentNotValidException ex, WebRequest request) { // Recibe la excepción y el request
        Map<String, String> errores = new HashMap<>(); // Mapa campo → mensaje de error
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> // Recorre los errores campo por campo
                errores.put(fieldError.getField(), fieldError.getDefaultMessage())); // Agrega el mensaje por campo
        log.warn("400 - Validación fallida: {}", errores); // Log WARN con el detalle
        Map<String, Object> cuerpo = new LinkedHashMap<>(); // Cuerpo de respuesta (orden conservado)
        cuerpo.put("timestamp", LocalDateTime.now()); // Marca de tiempo del error
        cuerpo.put("status", HttpStatus.BAD_REQUEST.value()); // Código numérico (400)
        cuerpo.put("error", "Validación fallida"); // Texto descriptivo
        cuerpo.put("message", "La petición contiene datos inválidos"); // Mensaje general
        cuerpo.put("path", request.getDescription(false).replace("uri=", "")); // Ruta donde ocurrió
        cuerpo.put("errors", errores); // Detalle campo por campo
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cuerpo); // Devuelve 400 con el cuerpo
    }

    @ExceptionHandler(IllegalArgumentException.class) // Captura IllegalArgumentException (por compatibilidad)
    public ResponseEntity<Map<String, Object>> manejarIlegalArgument( // Retorna 400 Bad Request
            IllegalArgumentException ex, WebRequest request) { // Recibe la excepción y el request
        log.warn("400 - Argumento ilegal: {}", ex.getMessage()); // Log WARN
        return construirRespuesta(HttpStatus.BAD_REQUEST, ex.getMessage(), request); // Construye la respuesta 400
    }

    @ExceptionHandler(Exception.class) // Red de seguridad: cualquier otra excepción no capturada
    public ResponseEntity<Map<String, Object>> manejarGenerico( // Retorna 500 Internal Server Error
            Exception ex, WebRequest request) { // Recibe la excepción y el request
        log.error("500 - Error inesperado", ex); // Log ERROR con stack trace completo
        return construirRespuesta(HttpStatus.INTERNAL_SERVER_ERROR, // Código 500
                "Error interno en el servidor: " + ex.getMessage(), // Mensaje controlado para el cliente
                request); // Ruta de la petición
    }

    // --- Método de apoyo: arma el cuerpo JSON común de todas las respuestas de error ---

    private ResponseEntity<Map<String, Object>> construirRespuesta( // Crea el ResponseEntity con el cuerpo estándar
            HttpStatus status, String mensaje, WebRequest request) { // Parámetros: código HTTP, mensaje y request
        Map<String, Object> cuerpo = new LinkedHashMap<>(); // Mapa ordenado para el cuerpo JSON
        cuerpo.put("timestamp", LocalDateTime.now()); // Marca de tiempo del error
        cuerpo.put("status", status.value()); // Código numérico (ej: 404)
        cuerpo.put("error", status.getReasonPhrase()); // Texto descriptivo del código (ej: "Not Found")
        cuerpo.put("message", mensaje); // Mensaje propio de la excepción
        cuerpo.put("path", request.getDescription(false).replace("uri=", "")); // Ruta donde ocurrió (sin el prefijo "uri=")
        return ResponseEntity.status(status).body(cuerpo); // Devuelve el ResponseEntity con el código y cuerpo
    }
}
