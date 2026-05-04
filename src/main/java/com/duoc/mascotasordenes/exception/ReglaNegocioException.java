package com.duoc.mascotasordenes.exception;

/**
 * Violación de una regla de negocio (stock insuficiente, transición de estado inválida,
 * orden cancelada, etc.). Mapeada a HTTP 400 por el {@link GlobalExceptionHandler}.
 */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
