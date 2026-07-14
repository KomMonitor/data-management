package de.hsbo.kommonitor.datamanagement.api.impl.exception;

public class ApiException extends Exception{
    private int code;

    /**
     *
     * @param code
     * @param msg
     */
    public ApiException (int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
