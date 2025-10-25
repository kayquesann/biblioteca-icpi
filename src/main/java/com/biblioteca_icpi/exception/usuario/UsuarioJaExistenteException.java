package com.biblioteca_icpi.exception.usuario;

public class UsuarioJaExistenteException extends RuntimeException {

    public  UsuarioJaExistenteException(String message) {
        super(message);
    }
}
