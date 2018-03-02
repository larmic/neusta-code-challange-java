package de.neusta.ncc.application;

import de.neusta.ncc.application.validator.LdapUserUniqueValidator;
import de.neusta.ncc.application.validator.RoomNumberValidator;
import de.neusta.ncc.application.validator.RoomUniqueValidator;
import de.neusta.ncc.application.validator.exception.LdapUserIsNotUniqueException;
import de.neusta.ncc.application.validator.exception.RoomIsNotUniqueException;
import de.neusta.ncc.application.validator.exception.RoomNumberNotValidException;
import de.neusta.ncc.domain.Person;
import de.neusta.ncc.domain.PersonTitle;
import de.neusta.ncc.domain.Room;
import de.neusta.ncc.infrastructure.CacheRoomRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

public class RoomImportServiceTest {

    private final CacheRoomRepository roomRepository = new CacheRoomRepository();
    private RoomImportService roomImportService;

    private Person susanne;
    private Person uwe;
    private Person alex;
    private Person samin;

    @Before
    public void setUp() {
        roomImportService = new RoomImportService(
                new RoomNumberValidator(),
                new RoomUniqueValidator(),
                new LdapUserUniqueValidator(),
                roomRepository);

        susanne = new Person.PersonBuilder("Susanne", "Moog", "smoog").build();
        uwe = new Person.PersonBuilder("Uwe", "Svensson", "usvens").title(PersonTitle.DR).build();
        alex = new Person.PersonBuilder("Alexander", "Cole", "acole").secondFirstName("James").build();
        samin = new Person.PersonBuilder("Samin", "Ölker", "soelker").build();
    }

    @Test
    public void testImport() {
        final Room room1 = new Room.RoomBuilder("1000").persons(Arrays.asList(susanne, uwe)).build();
        final Room room2 = new Room.RoomBuilder("1001").persons(Arrays.asList(alex, samin)).build();

        roomImportService.importRooms(Arrays.asList(room1, room2));

        assertThat(roomRepository.getRooms()).hasSize(2);
        assertThat(roomRepository.findByRoomNumber("1000").getRoomNumber()).isEqualTo("1000");
        assertThat(roomRepository.findByRoomNumber("1000").getPersons()).extracting("firstName", "lastName", "ldapUser")
                .containsExactlyInAnyOrder(
                        tuple("Susanne", "Moog", "smoog"),
                        tuple("Uwe", "Svensson", "usvens")
                );
        assertThat(roomRepository.findByRoomNumber("1001").getRoomNumber()).isEqualTo("1001");
        assertThat(roomRepository.findByRoomNumber("1001").getPersons()).extracting("firstName", "lastName", "ldapUser")
                .containsExactlyInAnyOrder(
                        tuple("Alexander James", "Cole", "acole"),
                        tuple("Samin", "Ölker", "soelker")
                );
    }

    @Test
    public void testImportWithWrongRoomNumberLength() {
        final Room room = new Room.RoomBuilder("100").persons(Arrays.asList(susanne, uwe)).build();

        try {
            roomImportService.importRooms(Collections.singletonList(room));
            fail("Should throw exception");
        } catch (RoomNumberNotValidException e) {
            assertThat(e.getMessage()).isEqualTo("Room with number 100 must have 4 arbitrary characters.");
        }
        assertThat(roomRepository.getRooms()).hasSize(0); // assert that nothing has been imported
    }

    @Test
    public void testImportWithRoomNumberIsNotUnique() {
        final Room room1 = new Room.RoomBuilder("1001").persons(Arrays.asList(susanne, uwe)).build();
        final Room room2 = new Room.RoomBuilder("1001").persons(Arrays.asList(alex, samin)).build();

        try {
            roomImportService.importRooms(Arrays.asList(room1, room2));
            fail("Should throw exception");
        } catch (RoomIsNotUniqueException e) {
            assertThat(e.getMessage()).isEqualTo("Room numbers should only appear once.");
        }

        assertThat(roomRepository.getRooms()).hasSize(0); // assert that nothing has been imported
    }

    @Test
    public void testImportWithRoomNumberIsNotUniqueAndRoomsAreEmpty() {
        final Room room1 = new Room.RoomBuilder("1001").build();
        final Room room2 = new Room.RoomBuilder("1001").build();

        try {
            roomImportService.importRooms(Arrays.asList(room1, room2));
            fail("Should throw exception");
        } catch (RoomIsNotUniqueException e) {
            assertThat(e.getMessage()).isEqualTo("Room numbers should only appear once.");
        }

        assertThat(roomRepository.getRooms()).hasSize(0); // assert that nothing has been imported
    }

    @Test
    public void testImportWithPersonIsNotUnique() {
        final Person saminWithSusannesLdap = new Person.PersonBuilder("Samin", "Ölker", "smoog").build();

        final Room room1 = new Room.RoomBuilder("1000").persons(Arrays.asList(susanne, uwe)).build();
        final Room room2 = new Room.RoomBuilder("1001").persons(Arrays.asList(alex, saminWithSusannesLdap)).build();

        try {
            roomImportService.importRooms(Arrays.asList(room1, room2));
            fail("Should throw exception");
        } catch (LdapUserIsNotUniqueException e) {
            assertThat(e.getMessage()).isEqualTo("LDAP users should only appear once.");
        }

        assertThat(roomRepository.getRooms()).hasSize(0); // assert that nothing has been imported
    }
}