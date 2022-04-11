package de.neusta.ncc.infrastructure;

import de.neusta.ncc.domain.Person;
import de.neusta.ncc.domain.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CacheRoomRepositoryTest {

    private CacheRoomRepository roomRepository;

    private Room room1;
    private Room room2;

    @BeforeEach
    public void setUp() {
        roomRepository = new CacheRoomRepository();

        final Person alexander = new Person.PersonBuilder("Alexander", "Cole", "acole")
                .secondFirstName("James")
                .build();
        final Person alexandersBrother = new Person.PersonBuilder("Alexander", "Cole", "abcole")
                .secondFirstName("Brother")
                .build();

        room1 = new Room.RoomBuilder("1110")
                .persons(Collections.singletonList(alexander))
                .build();
        room2 = new Room.RoomBuilder("1111")
                .persons(Collections.singletonList(alexandersBrother))
                .build();
    }

    @Test
    public void testPersist() {
        roomRepository.replaceRooms(Arrays.asList(room1, room2));

        assertThat(roomRepository.getRooms()).containsExactlyInAnyOrder(room1, room2);

        // verify list of rooms will be replaced on each replaceRooms
        roomRepository.replaceRooms(Collections.singletonList(room1));

        assertThat(roomRepository.getRooms()).containsExactly(room1);
    }

    @Test
    public void testGetRoomsIsUnmodifiable() {
        roomRepository.replaceRooms(Collections.singletonList(room1));

        assertThatThrownBy(() -> roomRepository.getRooms().add(room2))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage(null);
    }

    @Test
    public void testGetRoomsIsUnmodifiableOnInit() {
        assertThatThrownBy(() -> roomRepository.getRooms().add(room1))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage(null);
    }

    @Test
    public void testFindByRoomNumber() {
        roomRepository.replaceRooms(Arrays.asList(room1, room2));

        assertThat(roomRepository.findByRoomNumber("1110")).isEqualTo(room1);
        assertThat(roomRepository.findByRoomNumber("1111")).isEqualTo(room2);
    }

    @Test
    public void testFindByRoomNumberWithNotExisting() {
        assertThat(roomRepository.findByRoomNumber("0000")).isNull();
    }

    @Test
    public void testFindByLikeLdapUser() {
        roomRepository.replaceRooms(Arrays.asList(room1, room2));

        assertThat(roomRepository.findByLikeLdapUser("acole")).containsExactly(room1);
        assertThat(roomRepository.findByLikeLdapUser("aCole")).containsExactly(room1);
        assertThat(roomRepository.findByLikeLdapUser("abcole")).containsExactly(room2);
        assertThat(roomRepository.findByLikeLdapUser("cole")).containsExactlyInAnyOrder(room1, room2);
        assertThat(roomRepository.findByLikeLdapUser("unkown")).isEmpty();
    }

    @Test
    public void testFindByLikeLdapUserWithLdapUserIsEmpty() {
        roomRepository.replaceRooms(Arrays.asList(room1, room2));

        assertThat(roomRepository.findByLikeLdapUser("")).isEmpty();
    }

    @Test
    public void testFindByLikeLdapUserWithLdapUserIsNull() {
        roomRepository.replaceRooms(Arrays.asList(room1, room2));

        assertThat(roomRepository.findByLikeLdapUser(null)).isEmpty();
    }
}