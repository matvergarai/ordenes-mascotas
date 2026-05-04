package com.duoc.mascotasordenes.exception;

/**
 * Recurso buscado por id (o clave única) inexistente en BD. Mapeada a HTTP 404 por el
 * {@link GlobalExceptionHandler}.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
