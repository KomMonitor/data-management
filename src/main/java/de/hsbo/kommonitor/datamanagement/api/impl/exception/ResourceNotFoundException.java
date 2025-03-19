package de.hsbo.kommonitor.datamanagement.api.impl.exception;

/**
 *
 * @author Andreas
 */
@jakarta.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-01-30T08:21:53.602Z")

public class ResourceNotFoundException extends ApiException {
    private int code;

    /**
     *
     * @param code
     * @param msg
     */
    public ResourceNotFoundException (int code, String msg) {
        super(code, msg);
        this.code = code;
    }
}
