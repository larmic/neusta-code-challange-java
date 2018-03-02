package de.neusta.ncc.infrastructure;

import de.neusta.ncc.application.RoomImportService;
import de.neusta.ncc.infrastructure.mapper.exception.CsvPersonNotValidException;
import de.neusta.ncc.application.validator.exception.LdapUserIsNotUniqueException;
import de.neusta.ncc.application.validator.exception.RoomNumberNotValidException;
import de.neusta.ncc.application.validator.exception.RoomIsNotUniqueException;
import de.neusta.ncc.infrastructure.mapper.exception.EmptyFileImportException;
import de.neusta.ncc.infrastructure.mapper.exception.FileImportException;
import de.neusta.ncc.infrastructure.mapper.CsvImportMapper;
import de.neusta.ncc.infrastructure.dto.DefaultSpringErrorDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * Mapping and validation tests of {@link UploadController}.
 * <p>
 * Mocks inner mapper and services to be loosely coupled from core logic.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UploadControllerTest {

    @Autowired
    private UploadRequestSender uploadRequestSender;

    @MockBean
    private RoomImportService roomImportServiceMock;

    @MockBean
    private CsvImportMapper csvImportMapperMock;

    @Test
    public void testUploadWithSuccess() {
        final ResponseEntity<String> exchange = uploadRequestSender.sendUploadRequest("simple.csv", String.class);

        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(exchange.getBody()).isEqualTo("{\"uploadedFileName\":\"simple.csv\"}");
    }

    @Test
    public void testUploadWithFileIsEmpty() {
        when(csvImportMapperMock.map(any(MultipartFile.class))).thenThrow(new EmptyFileImportException());

        final ResponseEntity<String> exchange = uploadRequestSender.sendUploadRequest("empty.csv", String.class);

        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exchange.getBody()).isEqualTo("{\"code\":0,\"message\":\"Required request part 'file' is empty\"}");
    }

    @Test
    public void testUploadWithFileIsNull() {
        final ResponseEntity<String> exchange = uploadRequestSender.sendUploadRequest(null, String.class);

        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exchange.getBody()).contains("\"message\":\"Required request part 'file' is not present\"");
    }

    @Test
    public void testUploadWithCsvImportMapperThrowsGeneralImportException() {
        when(csvImportMapperMock.map(any(MultipartFile.class))).thenThrow(new FileImportException());

        final ResponseEntity<String> exchange = uploadRequestSender.sendUploadRequest("simple.csv", String.class);

        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exchange.getBody()).isEqualTo("{\"code\":0,\"message\":\"Access error when get bytes of file.\"}");
    }

    @Test
    public void testUploadWithCsvImportPersonIsNotValid() {
        doThrow(new CsvPersonNotValidException("test")).when(roomImportServiceMock).importRooms(any());

        final ResponseEntity<String> exchange = uploadRequestSender.sendUploadRequest("simple.csv", String.class);

        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exchange.getBody()).isEqualTo("{\"code\":4,\"message\":\"Could not map csv person 'test'\"}");
    }

    @Test
    public void testUploadWithWrongRoomNumberLength() {
        doThrow(new RoomNumberNotValidException("100")).when(roomImportServiceMock).importRooms(any());

        final ResponseEntity<String> exchange = uploadRequestSender.sendUploadRequest("simple.csv", String.class);

        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exchange.getBody()).isEqualTo("{\"code\":6,\"message\":\"Room with number 100 must have 4 arbitrary characters.\"}");
    }

    @Test
    public void testUploadWithRoomNumberIsNotUnique() {
        doThrow(new RoomIsNotUniqueException()).when(roomImportServiceMock).importRooms(any());

        final ResponseEntity<String> exchange = uploadRequestSender.sendUploadRequest("simple.csv", String.class);

        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exchange.getBody()).isEqualTo("{\"code\":2,\"message\":\"Room numbers should only appear once.\"}");
    }

    @Test
    public void testUploadWithPersonIsNotUnique() {
        doThrow(new LdapUserIsNotUniqueException()).when(roomImportServiceMock).importRooms(any());

        final ResponseEntity<String> exchange = uploadRequestSender.sendUploadRequest("simple.csv", String.class);

        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exchange.getBody()).isEqualTo("{\"code\":3,\"message\":\"LDAP users should only appear once.\"}");
    }

    @Test
    public void testUploadWithWrongMethodType() {
        assertMethodNotAllowedForUpload(HttpMethod.DELETE);
        assertMethodNotAllowedForUpload(HttpMethod.GET);
        assertMethodNotAllowedForUpload(HttpMethod.PUT);
    }

    private void assertMethodNotAllowedForUpload(HttpMethod httpMethod) {
        final ResponseEntity<DefaultSpringErrorDto> exchange = uploadRequestSender.sendUploadRequest("simple.csv", httpMethod, DefaultSpringErrorDto.class);

        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
        assertThat(exchange.getBody().getTimestamp()).isNotEmpty();
        assertThat(exchange.getBody().getStatus()).isEqualTo("405");
        assertThat(exchange.getBody().getError()).isEqualTo("Method Not Allowed");
        assertThat(exchange.getBody().getMessage()).isEqualTo("Request method '" + httpMethod.name() + "' not supported");
        assertThat(exchange.getBody().getPath()).isEqualTo("/api/import");
    }

}