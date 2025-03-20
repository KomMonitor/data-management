package de.hsbo.kommonitor.datamanagement.api.impl.exception;

/**
 *
 * @author Andreas
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-30T08:21:53.602Z")

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
}
