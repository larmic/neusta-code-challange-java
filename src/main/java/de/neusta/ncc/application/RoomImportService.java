package de.neusta.ncc.application;

import de.neusta.ncc.application.validator.LdapUserUniqueValidator;
import de.neusta.ncc.application.validator.RoomNumberValidator;
import de.neusta.ncc.application.validator.RoomUniqueValidator;
import de.neusta.ncc.application.validator.exception.LdapUserIsNotUniqueException;
import de.neusta.ncc.application.validator.exception.RoomIsNotUniqueException;
import de.neusta.ncc.application.validator.exception.RoomNumberNotValidException;
import de.neusta.ncc.domain.Person;
import de.neusta.ncc.domain.Room;
import de.neusta.ncc.domain.RoomRepository;
import de.neusta.ncc.infrastructure.mapper.exception.CsvPersonNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Imports given list of {@link Room} to repository. Existing rooms will be cleared.
 * If importing rooms are not valid (i.e. uniqueness of rooms or person) nothing will be imported.
 */
@Service
public class RoomImportService {

    private final RoomNumberValidator roomNumberValidator;
    private final RoomUniqueValidator roomUniqueValidator;
    private final LdapUserUniqueValidator ldapUserUniqueValidator;
    private final RoomRepository roomRepository;

    @Autowired
    public RoomImportService(RoomNumberValidator roomNumberValidator,
                             RoomUniqueValidator roomUniqueValidator,
                             LdapUserUniqueValidator ldapUserUniqueValidator,
                             RoomRepository roomRepository) {
        this.roomNumberValidator = roomNumberValidator;
        this.roomUniqueValidator = roomUniqueValidator;
        this.ldapUserUniqueValidator = ldapUserUniqueValidator;
        this.roomRepository = roomRepository;
    }

    public void importRooms(List<Room> rooms)
            throws CsvPersonNotValidException, RoomNumberNotValidException, RoomIsNotUniqueException, LdapUserIsNotUniqueException {
        validateRoomNumber(rooms);
        validateRoomNumbersAreUnique(rooms);
        validateLdapUsersAreUnique(rooms);

        roomRepository.replaceRooms(rooms);
    }

    private void validateRoomNumber(List<Room> rooms) {
        rooms.forEach(r -> roomNumberValidator.validate(r.getRoomNumber()));
    }

    private void validateRoomNumbersAreUnique(List<Room> rooms) {
        final List<String> roomNumbers = rooms.stream()
                .map(Room::getRoomNumber)
                .collect(Collectors.toList());

        roomUniqueValidator.validate(roomNumbers);
    }

    private void validateLdapUsersAreUnique(List<Room> rooms) {
        final List<String> ldapUsers = rooms.stream()
                .flatMap(r -> r.getPersons().stream())
                .map(Person::getLdapUser)
                .collect(Collectors.toList());

        ldapUserUniqueValidator.validate(ldapUsers);
    }
}
