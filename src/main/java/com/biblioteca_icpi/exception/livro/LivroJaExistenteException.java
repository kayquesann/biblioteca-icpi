package com.biblioteca_icpi.exception.livro;

public class LivroJaExistenteException extends RuntimeException{

    public LivroJaExistenteException (String message) {
        super(message);
    }
}
