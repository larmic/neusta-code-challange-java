package de.neusta.ncc.infrastructure.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Error message dto to return specific error codes when a import fails or i.e. a room for a given number is not found.
 */
@ApiModel(value = "ErrorMessageDto", description = "Contains defined error code with depending message")
public class ErrorMessageDto {

    @ApiModelProperty(value = "specific error code", required = true, example = "6")
    private int code;

    @ApiModelProperty(value = "specific error message", required = true, example = "Room number must have 4 arbitrary characters.")
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
