package de.neusta.ncc.application.validator;

import de.neusta.ncc.application.validator.exception.RoomIsNotUniqueException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Validate each room is unique in given list of strings (with ignoring case sensitive).
 * Acceptance criteria: A room exists only once in an import file
 */
@Component
public class RoomUniqueValidator {

    public void validate(List<String> rooms) throws RoomIsNotUniqueException {
        if (!new StringListUniqueChecker().itemsUnique(rooms)) {
            throw new RoomIsNotUniqueException();
        }
    }

}
