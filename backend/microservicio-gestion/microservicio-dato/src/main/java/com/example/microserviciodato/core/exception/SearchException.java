package com.example.microserviciodato.core.exception;

/*Escenario de Uso: Utiliza SearchException para indicar problemas específicos relacionados con búsquedas. Por ejemplo:

   * Cuando un registro no se encuentra en la base de datos.
   * Cuando el servicio de búsqueda falla.
   * Cuando la consulta de búsqueda está mal formada.
   */
public class SearchException extends Exception{
    public SearchException() {
    }

    public SearchException(String message) {
        super(message);
    }

    public SearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchException(Throwable cause) {
        super(cause);
    }

    public SearchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
