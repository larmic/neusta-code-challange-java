package de.neusta.ncc.domain;

import java.util.Collections;
import java.util.List;

public class Room {

    private final String roomNumber;
    private final List<Person> persons;

    private Room(String roomNumber, List<Person> persons) {
        this.roomNumber = roomNumber;
        this.persons = Collections.unmodifiableList(persons);
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public static class RoomBuilder {
        private final String roomNumber;
        private List<Person> persons = Collections.emptyList();

        public RoomBuilder(String roomNumber) {
            this.roomNumber = roomNumber;
        }

        public RoomBuilder persons(List<Person> persons) {
            this.persons = Collections.unmodifiableList(persons);
            return this;
        }

        public Room build() {
            return new Room(roomNumber, persons);
        }
    }
}
