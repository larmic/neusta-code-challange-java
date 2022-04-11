package de.neusta.ncc.infrastructure;

import de.neusta.ncc.domain.Room;
import de.neusta.ncc.domain.RoomRepository;
import de.neusta.ncc.infrastructure.dto.ErrorMessageDto;
import de.neusta.ncc.infrastructure.dto.RoomDto;
import de.neusta.ncc.infrastructure.mapper.RoomMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RoomController {

    private final RoomRepository roomRepository;

    private final RoomMapper roomMapper;

    @Autowired
    public RoomController(RoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    @Operation(
            summary = "Loads all existing rooms.",
            description = "If a ldap user name query parameter is used all rooms with persons contains parts of this name will be returned."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "405", description = "Wrong method type"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @RequestMapping(value = "/api/room", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<List<RoomDto>> getAllRooms(@RequestParam(required = false) String ldapUser) {
        final List<Room> rooms = isLdapUserSet(ldapUser) ? findRoomsByLdapUser(ldapUser) : getAllRooms();

        final List<RoomDto> roomDtos = rooms.stream()
                .map(roomMapper::mapToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(roomDtos);
    }

    @Operation(summary = "Loads a specific room for a given number.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "404", description = "Room for number not found"),
                    @ApiResponse(responseCode = "405", description = "Wrong method type"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @RequestMapping(value = "/api/room/{number}", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<?> getRoom(@PathVariable String number) {
        if (!isRoomNumberValid(number)) {
            return new ResponseEntity<>(new ErrorMessageDto(6, String.format("Room with number %s must have 4 arbitrary characters.", number)), HttpStatus.BAD_REQUEST);
        }

        final Room room = roomRepository.findByRoomNumber(number);

        if (room == null) {
            return new ResponseEntity<>(new ErrorMessageDto(5, "Room not found."), HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(roomMapper.mapToDto(room));
    }

    private boolean isLdapUserSet(String ldapUser) {
        return ldapUser != null && ldapUser.length() > 0;
    }

    private boolean isRoomNumberValid(String number) {
        return number.length() == 4;
    }

    private List<Room> getAllRooms() {
        return roomRepository.getRooms();
    }

    private List<Room> findRoomsByLdapUser(String ldapUser) {
        return roomRepository.findByLikeLdapUser(ldapUser);
    }
}
