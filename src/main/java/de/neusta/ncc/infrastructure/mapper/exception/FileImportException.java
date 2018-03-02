package de.neusta.ncc.infrastructure.mapper.exception;

public class FileImportException extends RuntimeException {

    public FileImportException() {
        super("Access error when get bytes of file.", null);
    }

    public FileImportException(Throwable cause) {
        super("Access error when get bytes of file.", cause);
    }

}
