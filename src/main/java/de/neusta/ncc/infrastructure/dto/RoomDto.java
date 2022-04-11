package de.neusta.ncc.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Room dto to wrap inner data model from json representation,
 */
@JsonPropertyOrder({"room", "people"})
public class RoomDto {

    private final List<PeopleDto> people = new ArrayList<>();
    private String room;

    public List<PeopleDto> getPeople() {
        return people;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
