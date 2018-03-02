package de.neusta.ncc.application.validator.exception;

public class RoomNumberNotValidException extends RuntimeException {

    public RoomNumberNotValidException(String roomNumber) {
        super(String.format("Room with number %s must have 4 arbitrary characters.", roomNumber), null);
    }

}
