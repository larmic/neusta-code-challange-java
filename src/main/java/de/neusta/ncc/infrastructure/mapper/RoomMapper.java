package de.neusta.ncc.infrastructure.mapper;

import de.neusta.ncc.domain.Person;
import de.neusta.ncc.domain.Room;
import de.neusta.ncc.infrastructure.dto.PeopleDto;
import de.neusta.ncc.infrastructure.dto.RoomDto;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public RoomDto mapToDto(Room room) {
        final RoomDto roomDto = new RoomDto();

        roomDto.setRoom(room.getRoomNumber());

        room.getPersons().stream()
                .map(this::mapPersonToDto)
                .forEach(p -> roomDto.getPeople().add(p));

        return roomDto;
    }

    private PeopleDto mapPersonToDto(Person p) {
        final PeopleDto peopleDto = new PeopleDto();
        peopleDto.setFirstName(p.getFirstName());

        if (p.getAddition() != null) {
            peopleDto.setAddition(p.getAddition().getLabel());
        }

        peopleDto.setLastName(p.getLastName());
        peopleDto.setLdapUser(p.getLdapUser());

        if (p.getTitle() != null) {
            peopleDto.setTitle(p.getTitle().getLabel());
        }
        return peopleDto;
    }

}
