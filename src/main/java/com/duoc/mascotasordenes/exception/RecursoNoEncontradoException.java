package com.duoc.mascotasordenes.exception; // Paquete donde viven las excepciones personalizadas del dominio

/**
 * Excepción lanzada cuando se busca un recurso por ID (o campo único) y no existe en la base de datos.
 * El @ControllerAdvice la convierte en una respuesta HTTP 404 Not Found con cuerpo JSON coherente.
 */
public class RecursoNoEncontradoException extends RuntimeException { // RuntimeException: no obliga a declararla con throws

    public RecursoNoEncontradoException(String mensaje) { // Constructor que recibe el mensaje descriptivo
        super(mensaje); // Delega el mensaje al constructor de RuntimeException (se expone con getMessage())
    }
}
