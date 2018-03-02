package de.neusta.ncc.infrastructure;

import de.neusta.ncc.domain.Room;
import de.neusta.ncc.domain.RoomRepository;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableList;

/**
 * Simple embedded rooms cache. Could be replaced by a database. Up to now this is a very simple version of a storage.
 */
@Repository
public class CacheRoomRepository implements RoomRepository {

    private List<Room> rooms = Collections.emptyList();

    @Override
    public void replaceRooms(List<Room> rooms) {
        this.rooms = unmodifiableList(rooms);
    }

    @Override
    public Room findByRoomNumber(String roomNumber) {
        return rooms.stream()
                .filter(r -> roomNumber.equals(r.getRoomNumber()))
                .findFirst().orElse(null);
    }

    @Override
    public List<Room> findByLikeLdapUser(String ldapUser) {
        if (ldapUser == null || ldapUser.length() == 0) {
            return Collections.emptyList();
        }

        return rooms.stream()
                .filter(r -> roomContainsLikeLdapUserIgnoreCase(ldapUser, r))
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> getRooms() {
        return rooms;
    }

    private boolean roomContainsLikeLdapUserIgnoreCase(String ldapUser, Room r) {
        return r.getPersons().stream().anyMatch(p -> p.getLdapUser().toLowerCase().contains(ldapUser.toLowerCase()));
    }
}
