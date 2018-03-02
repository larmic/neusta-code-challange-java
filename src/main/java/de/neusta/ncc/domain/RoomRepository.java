package de.neusta.ncc.domain;

import java.util.List;

public interface RoomRepository {

    /**
     * Replaced already existing rooms by new collection of rooms
     */
    void replaceRooms(List<Room> rooms);

    /**
     * @return {@link Room} matching exactly given room number.
     */
    Room findByRoomNumber(String roomNumber);

    /**
     * @return a {@link List} of {@link Room}s that contains persons with given is like ldap user names.
     */
    List<Room> findByLikeLdapUser(String ldapUser);

    List<Room> getRooms();
}
