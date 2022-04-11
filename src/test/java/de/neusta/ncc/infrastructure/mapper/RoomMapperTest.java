package de.neusta.ncc.infrastructure.mapper;

import de.neusta.ncc.domain.Person;
import de.neusta.ncc.domain.PersonAddition;
import de.neusta.ncc.domain.PersonTitle;
import de.neusta.ncc.domain.Room;
import de.neusta.ncc.infrastructure.dto.RoomDto;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class RoomMapperTest {

    private RoomMapper roomMapper = new RoomMapper();

    @Test
    public void mapToDto() {
        final Person person1 = new Person.PersonBuilder("Leif", "Genzmer", "lgenzmer").title(PersonTitle.DR).secondFirstName("Arne").build();
        final Person person2 = new Person.PersonBuilder("Samin", "Ölker", "soelker").addition(PersonAddition.DE).build();

        final Room room = new Room.RoomBuilder("1103").persons(Arrays.asList(person1, person2)).build();

        final RoomDto roomDto = roomMapper.mapToDto(room);

        assertThat(roomDto.getRoom()).isEqualTo("1103");
        assertThat(roomDto.getPeople()).extracting("firstName", "lastName", "title", "addition", "ldapUser").containsExactlyInAnyOrder(tuple("Leif Arne", "Genzmer", "Dr.", null, "lgenzmer"), tuple("Samin", "Ölker", null, "de", "soelker"));
    }

    @Test
    public void mapToDtoWithRoomIsEmpty() {
        final Room room = new Room.RoomBuilder("1104").build();

        final RoomDto roomDto = roomMapper.mapToDto(room);

        assertThat(roomDto.getRoom()).isEqualTo("1104");
        assertThat(roomDto.getPeople()).isEmpty();
    }
}