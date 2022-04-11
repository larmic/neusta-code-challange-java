package de.neusta.ncc.infrastructure;

import de.neusta.ncc.domain.Room;
import de.neusta.ncc.domain.RoomRepository;
import de.neusta.ncc.infrastructure.dto.ImportResultDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

/**
 * Complete (happy path) integration test. For validation and mapping tests see {@link UploadControllerTest}.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UploadControllerIntegrationTest {

    @Autowired
    private UploadRequestSender uploadRequestSender;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void testUploadWithSuccess() {
        final ResponseEntity<ImportResultDto> exchange = uploadRequestSender.sendUploadRequest("simple.csv", ImportResultDto.class);

        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(exchange.getBody().getUploadedFileName()).isEqualTo("simple.csv");

        assertThat(roomRepository.getRooms()).hasSize(2);

        final Room room1111 = roomRepository.findByRoomNumber("1111");
        assertThat(room1111).isNotNull();
        assertThat(room1111.getRoomNumber()).isEqualTo("1111");
        assertThat(room1111.getPersons())
                .extracting("title", "firstName", "addition", "lastName", "ldapUser")
                .containsExactlyInAnyOrder(
                        tuple(null, "Dennis", null, "Fischer", "dfischer"),
                        tuple(null, "Frank", null, "Supper", "fsupper"),
                        tuple(null, "Susanne", null, "Moog", "smoog")
                );

        final Room room1110 = roomRepository.findByRoomNumber("1110");
        assertThat(room1110).isNotNull();
        assertThat(room1110.getRoomNumber()).isEqualTo("1110");
        assertThat(room1110.getPersons())
                .extracting("title", "firstName", "addition", "lastName", "ldapUser")
                .containsExactlyInAnyOrder(
                        tuple(null, "Christina", null, "Hülsemann", "chuelsemann"),
                        tuple(null, "Iftikar Ahmad", null, "Khan", "ikhan"),
                        tuple(null, "Mabelle", null, "Tengue", "mtengue"),
                        tuple(null, "Ralf", null, "Schmidt", "rschmidt")
                );

    }
}