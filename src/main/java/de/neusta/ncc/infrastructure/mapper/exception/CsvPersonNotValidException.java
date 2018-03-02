package de.neusta.ncc.infrastructure.mapper.exception;

public class CsvPersonNotValidException extends RuntimeException {

    public CsvPersonNotValidException(String csvOutboundPerson) {
        super(String.format("Could not map csv person '%s'", csvOutboundPerson));
    }

}
