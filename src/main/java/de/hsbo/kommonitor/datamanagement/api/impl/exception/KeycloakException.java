package de.hsbo.kommonitor.datamanagement.api.impl.exception;

public class KeycloakException extends Exception{

    public KeycloakException() {
    }

    public KeycloakException(String message) {
        super(message);
    }

    public KeycloakException(String message, Throwable cause) {
        super(message, cause);
    }
}
