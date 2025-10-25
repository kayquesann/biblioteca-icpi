package com.biblioteca_icpi.exception.livro;

public class LivroNaoEncontradoException extends RuntimeException{

    public LivroNaoEncontradoException (String message) {
        super(message);
    }
}
