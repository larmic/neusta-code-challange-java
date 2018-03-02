package de.neusta.ncc.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Room dto to wrap inner data model from json representation,
 */
@JsonPropertyOrder({"room", "people"})
@ApiModel(value = "RoomDto")
public class RoomDto {

    @ApiModelProperty(value = "List of all persons working in corresponding room", required = true)
    private final List<PeopleDto> people = new ArrayList<>();

    @ApiModelProperty(value = "Unique room number", required = true, example = "1000")
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
