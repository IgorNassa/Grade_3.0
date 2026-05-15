package br.sistema.model.exception;

public class DuplicateResourceExecption extends RuntimeException {
    public DuplicateResourceExecption(String message) {
        super(message);
    }

    public DuplicateResourceExecption(String message, Throwable cause){
        super(message,cause);
    }
}
