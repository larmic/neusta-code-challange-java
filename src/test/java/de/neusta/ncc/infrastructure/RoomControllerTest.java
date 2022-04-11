package de.neusta.ncc.infrastructure;

import de.neusta.ncc.domain.Person;
import de.neusta.ncc.domain.Room;
import de.neusta.ncc.domain.RoomRepository;
import de.neusta.ncc.infrastructure.dto.DefaultSpringErrorDto;
import de.neusta.ncc.infrastructure.dto.RoomDto;
import de.neusta.ncc.infrastructure.mapper.CsvPersonToPersonMapper;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoomControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CsvPersonToPersonMapper csvPersonToPersonMapper;

    @MockBean
    private RoomRepository roomRepositoryMock;

    @BeforeEach
    public void setUp() {
        when(roomRepositoryMock.getRooms()).thenReturn(Arrays.asList(
                createRoom("1000", "Susanne Moog (smoog)"),
                createRoom("1001", "Alexander James Cole (acole)", "Dr. Samin van Ölker (soelker)")
        ));
        when(roomRepositoryMock.findByRoomNumber("1000")).thenReturn(createRoom("1000", "Susanne Moog (smoog)"));
        when(roomRepositoryMock.findByRoomNumber("1001")).thenReturn(createRoom("1001", "Alexander James Cole (acole)", "Dr. Samin van Ölker (soelker)"));

        when(roomRepositoryMock.findByLikeLdapUser("smoog")).thenReturn(Collections.singletonList(createRoom("1000", "Susanne Moog (smoog)")));
    }

    @Test
    public void testGetAllRooms() {
        final ResponseEntity<List<RoomDto>> exchange = testRestTemplate.exchange("/api/room", HttpMethod.GET, null, new ParameterizedTypeReference<List<RoomDto>>() {
        });

        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(exchange.getBody()).extracting("room").containsExactlyInAnyOrder("1000", "1001");
        assertThat(getRoomFrom(exchange.getBody(), "1000").getPeople())
                .extracting("title", "firstName", "addition", "lastName", "ldapUser")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(null, "Susanne", null, "Moog", "smoog")
                );
        assertThat(getRoomFrom(exchange.getBody(), "1001").getPeople())
                .extracting("title", "firstName", "addition", "lastName", "ldapUser")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(null, "Alexander James", null, "Cole", "acole"),
                        Tuple.tuple("Dr.", "Samin", "van", "Ölker", "soelker")
                );
    }

    @Test
    public void testGetAllRoomsWithLdapQueryParam() {
        final ResponseEntity<List<RoomDto>> exchange = testRestTemplate.exchange("/api/room?ldapUser=smoog", HttpMethod.GET, null, new ParameterizedTypeReference<List<RoomDto>>() {
        });

        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(exchange.getBody()).extracting("room").containsExactly("1000");
        assertThat(getRoomFrom(exchange.getBody(), "1000").getPeople())
                .extracting("title", "firstName", "addition", "lastName", "ldapUser")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(null, "Susanne", null, "Moog", "smoog")
                );
    }

    @Test
    public void testGetAllRoomsWithLdapQueryParamIsEmpty() {
        final ResponseEntity<List<RoomDto>> exchange = testRestTemplate.exchange("/api/room?ldapUser=", HttpMethod.GET, null, new ParameterizedTypeReference<List<RoomDto>>() {
        });

        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(exchange.getBody()).extracting("room").containsExactlyInAnyOrder("1000", "1001");
        assertThat(getRoomFrom(exchange.getBody(), "1000").getPeople())
                .extracting("title", "firstName", "addition", "lastName", "ldapUser")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(null, "Susanne", null, "Moog", "smoog")
                );
        assertThat(getRoomFrom(exchange.getBody(), "1001").getPeople())
                .extracting("title", "firstName", "addition", "lastName", "ldapUser")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(null, "Alexander James", null, "Cole", "acole"),
                        Tuple.tuple("Dr.", "Samin", "van", "Ölker", "soelker")
                );
    }

    @Test
    public void testGetAllRoomsWithNoRoomsExists() {
        reset(roomRepositoryMock);

        final ResponseEntity<List<RoomDto>> exchange = testRestTemplate.exchange("/api/room", HttpMethod.GET, null, new ParameterizedTypeReference<List<RoomDto>>() {
        });

        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(exchange.getBody()).isEmpty();
        assertThat(exchange.getBody()).isNotNull();
    }

    @Test
    public void testGetRoom() {
        final ResponseEntity<RoomDto> room1000 = testRestTemplate.exchange("/api/room/1000", HttpMethod.GET, null, RoomDto.class);
        assertThat(room1000.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(room1000.getBody().getRoom()).isEqualTo("1000");
        assertThat(room1000.getBody().getPeople())
                .extracting("title", "firstName", "addition", "lastName", "ldapUser")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(null, "Susanne", null, "Moog", "smoog")
                );

        final ResponseEntity<RoomDto> room1001 = testRestTemplate.exchange("/api/room/1001", HttpMethod.GET, null, RoomDto.class);
        assertThat(room1001.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(room1001.getBody().getRoom()).isEqualTo("1001");
        assertThat(room1001.getBody().getPeople())
                .extracting("title", "firstName", "addition", "lastName", "ldapUser")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(null, "Alexander James", null, "Cole", "acole"),
                        Tuple.tuple("Dr.", "Samin", "van", "Ölker", "soelker")
                );
    }

    @Test
    public void testGetRoomWithWrongLength() {
        final ResponseEntity<String> exchangeLength1 = testRestTemplate.exchange("/api/room/1", HttpMethod.GET, null, String.class);
        assertThat(exchangeLength1.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exchangeLength1.getBody()).isEqualTo("{\"code\":6,\"message\":\"Room with number 1 must have 4 arbitrary characters.\"}");

        final ResponseEntity<String> exchangeLength2 = testRestTemplate.exchange("/api/room/10", HttpMethod.GET, null, String.class);
        assertThat(exchangeLength2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exchangeLength2.getBody()).isEqualTo("{\"code\":6,\"message\":\"Room with number 10 must have 4 arbitrary characters.\"}");

        final ResponseEntity<String> exchangeLength3 = testRestTemplate.exchange("/api/room/101", HttpMethod.GET, null, String.class);
        assertThat(exchangeLength3.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exchangeLength3.getBody()).isEqualTo("{\"code\":6,\"message\":\"Room with number 101 must have 4 arbitrary characters.\"}");

        final ResponseEntity<String> exchangeLength5 = testRestTemplate.exchange("/api/room/10151", HttpMethod.GET, null, String.class);
        assertThat(exchangeLength5.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exchangeLength5.getBody()).isEqualTo("{\"code\":6,\"message\":\"Room with number 10151 must have 4 arbitrary characters.\"}");

        final ResponseEntity<String> exchangeLength6 = testRestTemplate.exchange("/api/room/101512", HttpMethod.GET, null, String.class);
        assertThat(exchangeLength6.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exchangeLength6.getBody()).isEqualTo("{\"code\":6,\"message\":\"Room with number 101512 must have 4 arbitrary characters.\"}");
    }

    @Test
    public void testGetRoomWithNotFound() {
        final ResponseEntity<String> exchange = testRestTemplate.exchange("/api/room/1015", HttpMethod.GET, null, String.class);

        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exchange.getBody()).isEqualTo("{\"code\":5,\"message\":\"Room not found.\"}");
    }

    @Test
    public void testGetRoomWithWrongMethodType() {
        assertMethodNotAllowedForUpload(HttpMethod.DELETE, "/api/room/1000");
        assertMethodNotAllowedForUpload(HttpMethod.POST, "/api/room/1000");
        assertMethodNotAllowedForUpload(HttpMethod.PUT, "/api/room/1000");
    }

    @Test
    public void testGetAllRoomsWithWrongMethodType() {
        assertMethodNotAllowedForUpload(HttpMethod.DELETE, "/api/room");
        assertMethodNotAllowedForUpload(HttpMethod.POST, "/api/room");
        assertMethodNotAllowedForUpload(HttpMethod.PUT, "/api/room");
    }

    private Room createRoom(String roomNumber, String... persons) {
        final List<Person> per = Arrays.stream(persons).map(p -> csvPersonToPersonMapper.map(p)).collect(Collectors.toList());
        return new Room.RoomBuilder(roomNumber).persons(per).build();
    }

    private RoomDto getRoomFrom(List<RoomDto> rooms, String roomNumber) {
        return rooms.stream()
                .filter(r -> roomNumber.equals(r.getRoom()))
                .findAny().orElse(null);
    }


    private void assertMethodNotAllowedForUpload(HttpMethod httpMethod, String url) {
        final ResponseEntity<DefaultSpringErrorDto> exchange = testRestTemplate.exchange(url, httpMethod, null, DefaultSpringErrorDto.class);

        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
        assertThat(exchange.getBody().getTimestamp()).isNotEmpty();
        assertThat(exchange.getBody().getStatus()).isEqualTo("405");
        assertThat(exchange.getBody().getError()).isEqualTo("Method Not Allowed");
        assertThat(exchange.getBody().getPath()).isEqualTo(url);
    }
}