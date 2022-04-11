package de.neusta.ncc.infrastructure.dto;

/**
 * Just a documentation class for swagger to show a defined response on default spring boot rest errors.
 * I.e. method is called with wrong method type.
 */
public class DefaultSpringErrorDto {

    private String timestamp;
    private String status;
    private String error;
    private String exception;
    private String message;
    private String path;

    public String getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getException() {
        return exception;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

}