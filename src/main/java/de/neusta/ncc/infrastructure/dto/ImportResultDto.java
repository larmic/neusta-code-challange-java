package de.neusta.ncc.infrastructure.dto;

// HINT: maybe add HATEOAS links to all imported rooms?
public class ImportResultDto {

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
