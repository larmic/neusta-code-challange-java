package de.neusta.ncc.infrastructure.mapper;

import de.neusta.ncc.infrastructure.mapper.exception.EmptyFileImportException;
import de.neusta.ncc.infrastructure.mapper.exception.FileImportException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CsvImportMapperTest {

    private CsvImportMapper mapper = new CsvImportMapper(new CsvPersonToPersonMapper());

    @Test
    public void testMapWithEmptyFile() {
        assertThatThrownBy(() -> mapper.map(new MockMultipartFile("empty.csv", (byte[]) null)))
                .isInstanceOf(EmptyFileImportException.class)
                .hasMessageContaining("Required request part 'file' is empty");
    }

    @Test
    public void testMapNewWithEmptyFile() {
        assertThatThrownBy(() -> mapper.map(new MockMultipartFile("empty.csv", (byte[]) null)))
                .isInstanceOf(EmptyFileImportException.class)
                .hasMessageContaining("Required request part 'file' is empty");
    }

    @Test
    public void testMapWithEmptyRoom() throws Exception {
        final var dataModel = mapper.map(new MockMultipartFile("empty_room.csv", Files.readAllBytes(Paths.get("src/test/resources/upload/empty_room.csv"))));

        assertThat(dataModel).hasSize(3);
        assertThat(dataModel.get(0).getRoomNumber()).isEqualTo("1102");
        assertThat(dataModel.get(0).getPersons()).isEmpty();
        assertThat(dataModel.get(1).getRoomNumber()).isEqualTo("1103");
        assertThat(dataModel.get(1).getPersons()).isEmpty();
        assertThat(dataModel.get(2).getRoomNumber()).isEqualTo("1104");
        assertThat(dataModel.get(2).getPersons()).isEmpty();
    }

    @Test
    public void testMapSimple() throws Exception {
        final var dataModel = mapper.map(new MockMultipartFile("simple.csv", Files.readAllBytes(Paths.get("src/test/resources/upload/simple.csv"))));

        assertThat(dataModel).hasSize(2);
        assertThat(dataModel.get(0).getRoomNumber()).isEqualTo("1111");
        assertThat(dataModel.get(0).getPersons())
                .extracting("firstName", "lastName", "ldapUser")
                .containsOnlyOnce(
                        tuple("Dennis", "Fischer", "dfischer"),
                        tuple("Frank", "Supper", "fsupper"),
                        tuple("Susanne", "Moog", "smoog")
                );
        assertThat(dataModel.get(1).getRoomNumber()).isEqualTo("1110");
        assertThat(dataModel.get(1).getPersons())
                .extracting("firstName", "lastName", "ldapUser")
                .containsOnlyOnce(
                        tuple("Christina", "Hülsemann", "chuelsemann"),
                        tuple("Iftikar Ahmad", "Khan", "ikhan"),
                        tuple("Mabelle", "Tengue", "mtengue"),
                        tuple("Ralf", "Schmidt", "rschmidt")
                );
    }

    /**
     * No blackbox test. {@link MultipartFile#getBytes()} throws checked {@link IOException} so this test ensures
     * exception will be handled correctly.
     */
    @Test
    public void testMapWithGetBytesThrowsIOException() throws Exception {
        final MultipartFile multipartFileMock = mock(MultipartFile.class);
        when(multipartFileMock.getBytes()).thenThrow(new IOException());
        when(multipartFileMock.isEmpty()).thenReturn(false);

        assertThatThrownBy(() -> mapper.map(multipartFileMock))
                .isInstanceOf(FileImportException.class);
    }
}