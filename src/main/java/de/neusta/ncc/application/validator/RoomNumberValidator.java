package de.neusta.ncc.application.validator;

import de.neusta.ncc.application.validator.exception.RoomNumberNotValidException;
import org.springframework.stereotype.Component;

/**
 * Validate length of a room number.
 * Acceptance criteria: 4 arbitrary characters
 */
@Component
public class RoomNumberValidator {

    public void validate(String room) throws RoomNumberNotValidException {
        if (!(room != null && room.length() == 4)) {
            throw new RoomNumberNotValidException(room);
        }
    }

}
