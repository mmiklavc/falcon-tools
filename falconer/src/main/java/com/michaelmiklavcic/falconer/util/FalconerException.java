package com.michaelmiklavcic.falconer.util;

public class FalconerException extends RuntimeException {

    public FalconerException(String message) {
        super(message);
    }

    public FalconerException(String message, Throwable cause) {
        super(message, cause);
    }

}
