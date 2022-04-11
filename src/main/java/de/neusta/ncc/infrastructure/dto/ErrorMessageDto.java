package de.neusta.ncc.infrastructure.dto;

/**
 * Error message dto to return specific error codes when a import fails or i.e. a room for a given number is not found.
 */
public class ErrorMessageDto {

    private int code;
    private String message;

    public ErrorMessageDto(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
