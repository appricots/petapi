package com.disney.studios.service;

public class DuplicateVoteException extends RuntimeException  {
    public DuplicateVoteException() {
        super();
    }

    public DuplicateVoteException(String message) {
        super(message);
    }

    public DuplicateVoteException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateVoteException(Throwable cause) {
        super(cause);
    }
}

