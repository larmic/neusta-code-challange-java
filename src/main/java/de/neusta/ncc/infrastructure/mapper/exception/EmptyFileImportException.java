package de.neusta.ncc.infrastructure.mapper.exception;

public class EmptyFileImportException extends RuntimeException {

    public EmptyFileImportException() {
        super("Required request part 'file' is empty");
    }

}
