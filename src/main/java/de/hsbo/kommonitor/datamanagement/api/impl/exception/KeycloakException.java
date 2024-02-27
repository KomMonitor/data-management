package de.hsbo.kommonitor.datamanagement.api.impl.exception;

public class KeycloakException extends ApiException{

    /**
     * @param code
     * @param msg
     */
    public KeycloakException(int code, String msg) {
        super(code, msg);
    }
}
