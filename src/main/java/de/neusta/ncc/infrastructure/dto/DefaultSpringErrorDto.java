package de.neusta.ncc.infrastructure.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Just a documentation class for swagger to show a defined response on default spring boot rest errors.
 * I.e. method is called with wrong method type.
 */
@ApiModel(value = "DefaultSpringErrorDto", description = "This is the default error class spring boot is returning, when an error occurs")
public class DefaultSpringErrorDto {

    @ApiModelProperty(value = "Timestamp in milles when the error occurred", required = true, example = "14443434344")
    private String timestamp;

    @ApiModelProperty(value = "Http status", required = true, example = "405")
    private String status;

    @ApiModelProperty(value = "Short error message", required = true, example = "Method Not Allowed")
    private String error;

    @ApiModelProperty(value = "returning exception", required = true, example = "org.springframework.web.HttpRequestMethodNotSupportedException")
    private String exception;

    @ApiModelProperty(value = "specific error message", required = true, example = "Request method 'DELETE' not supported")
    private String message;

    @ApiModelProperty(value = "requested url", required = true, example = "/api/room/1000")
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