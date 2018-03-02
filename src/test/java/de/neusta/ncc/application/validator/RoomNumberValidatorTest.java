package de.neusta.ncc.application.validator;

import de.neusta.ncc.application.validator.exception.RoomNumberNotValidException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class RoomNumberValidatorTest {

    private final RoomNumberValidator validator = new RoomNumberValidator();

    @Test
    public void validate() {
        validator.validate("1234");
        validator.validate("ABCF");
        validator.validate("/[]{");
        validator.validate("ÄÜÖ?");
    }

    @Test
    public void validateWithTooShort() {
        assertException("123");
        assertException("AB");
        assertException("/");
    }

    @Test
    public void validateWithTooLong() {
        assertException("1234Z");
        assertException("ABCFZZ");
        assertException("/[]{ZZZ");
        assertException("ÄÜÖ?ZZZZ");
    }

    @Test(expected = RoomNumberNotValidException.class)
    public void validateWithEmptyValue() {
        validator.validate("");
    }

    @Test(expected = RoomNumberNotValidException.class)
    public void validateWithNullValue() {
        validator.validate(null);
    }

    private void assertException(String room) {
        try {
            validator.validate(room);
            fail("Should throw exception");
        } catch (RoomNumberNotValidException e) {
            assertThat(e.getMessage()).isEqualTo("Room with number " + room + " must have 4 arbitrary characters.");
        }
    }
}