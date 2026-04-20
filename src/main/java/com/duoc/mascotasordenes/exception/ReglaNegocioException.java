package com.duoc.mascotasordenes.exception; // Paquete donde viven las excepciones personalizadas del dominio

/**
 * Excepción lanzada cuando se viola una regla de negocio (por ejemplo: stock insuficiente, producto inexistente,
 * estado inválido, etc.). El @ControllerAdvice la convierte en una respuesta HTTP 400 Bad Request con cuerpo JSON .
 */
public class ReglaNegocioException extends RuntimeException { // RuntimeException: no obliga a declararla con throws

    public ReglaNegocioException(String mensaje) { // Constructor que recibe el mensaje descriptivo del error de negocio
        super(mensaje); // Delega el mensaje al constructor de RuntimeException (se expone con getMessage())
    }
}
