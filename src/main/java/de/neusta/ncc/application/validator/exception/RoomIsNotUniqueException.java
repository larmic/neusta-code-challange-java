package de.neusta.ncc.application.validator.exception;

public class RoomIsNotUniqueException extends RuntimeException {

    public RoomIsNotUniqueException() {
        super("Room numbers should only appear once.", null);
    }

}
