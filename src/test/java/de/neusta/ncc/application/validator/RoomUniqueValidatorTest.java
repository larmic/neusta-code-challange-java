package de.neusta.ncc.application.validator;

import de.neusta.ncc.application.validator.exception.RoomIsNotUniqueException;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class RoomUniqueValidatorTest {

    private final RoomUniqueValidator validator = new RoomUniqueValidator();

    @Test
    public void validate() {
        validator.validate(Collections.emptyList());
        validator.validate(Collections.singletonList("1234"));
        validator.validate(Arrays.asList("1234", "1235"));
        validator.validate(Arrays.asList("1234", "1236"));
        validator.validate(Arrays.asList("1234", "1235", "1236"));
    }

    @Test
    public void validateWithDuplicates() {
        assertException(Arrays.asList("1234", "1234"));
        assertException(Arrays.asList("1234", "1235", "1234"));
        assertException(Arrays.asList("1234", "1235", "1235", "1236"));
    }

    private void assertException(List<String> rooms) {
        try {
            validator.validate(rooms);
            fail("Should throw exception");
        } catch (RoomIsNotUniqueException e) {
            assertThat(e.getMessage()).isEqualTo("Room numbers should only appear once.");
        }
    }
}