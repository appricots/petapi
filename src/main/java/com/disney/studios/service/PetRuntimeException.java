package com.disney.studios.service;

public class PetRuntimeException extends RuntimeException  {
    public PetRuntimeException() {
        super();
    }

    public PetRuntimeException(String message) {
        super(message);
    }

    public PetRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PetRuntimeException(Throwable cause) {
        super(cause);
    }
}

