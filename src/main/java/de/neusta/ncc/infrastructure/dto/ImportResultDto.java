package de.neusta.ncc.infrastructure.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

// HINT: maybe add HATEOAS links to all imported rooms?
@ApiModel(value = "ImportResultDto", description = "File upload response")
public class ImportResultDto {

    @ApiModelProperty(value = "File name of the uploaded file", required = true, example = "sitzplan.csv")
    private String uploadedFileName;

    private ImportResultDto() {
        // spring requires a default constructor for unmarshalling
    }

    public ImportResultDto(String uploadedFileName) {
        this.uploadedFileName = uploadedFileName;
    }

    public String getUploadedFileName() {
        return uploadedFileName;
    }
}
