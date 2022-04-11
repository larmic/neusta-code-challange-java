package de.neusta.ncc.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class RoomBuilderTest {

    @Test
    public void testRoomNumber() {
        final Room room = new Room.RoomBuilder("1234").build();

        assertThat(room.getRoomNumber()).isEqualTo("1234");
        assertThat(room.getPersons()).isEmpty();
        assertThat(room.getPersons()).isNotNull();
    }

    @Test
    public void testPersons() {
        final Person susanne = new Person.PersonBuilder("Susanne", "Moog", "smoog").build();
        final Person uwe = new Person.PersonBuilder("Uwe", "Svensson", "usvens").title(PersonTitle.DR).build();
        final Person alex = new Person.PersonBuilder("Alexander", "Cole", "acole").secondFirstName("James").build();
        final Person florenz = new Person.PersonBuilder("Florenz", "Buhrke", "fbuhrke").addition(PersonAddition.VON).build();

        final Room room = new Room.RoomBuilder("4444")
                .persons(Arrays.asList(susanne, uwe, alex, florenz))
                .build();

        assertThat(room.getRoomNumber()).isEqualTo("4444");
        assertThat(room.getPersons()).containsExactlyInAnyOrder(susanne, uwe, alex, florenz);
    }

}