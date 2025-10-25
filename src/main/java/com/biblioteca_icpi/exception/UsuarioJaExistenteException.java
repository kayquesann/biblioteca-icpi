package com.biblioteca_icpi.exception;

public class UsuarioJaExistenteException extends RuntimeException {

    public  UsuarioJaExistenteException(String message) {
        super(message);
    }
}
