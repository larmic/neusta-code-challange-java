package de.neusta.ncc.application.validator;

import de.neusta.ncc.application.validator.exception.RoomNumberNotValidException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

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

    @Test
    public void validateWithEmptyValue() {
        assertThatThrownBy(() -> validator.validate(""))
                .isInstanceOf(RoomNumberNotValidException.class)
                .hasMessageContaining("Room with number  must have 4 arbitrary characters.");
    }

    @Test
    public void validateWithNullValue() {
        assertThatThrownBy(() -> validator.validate(null))
                .isInstanceOf(RoomNumberNotValidException.class)
                .hasMessageContaining("Room with number null must have 4 arbitrary characters.");
    }

    private void assertException(String room) {
        assertThatThrownBy(() -> validator.validate(room))
                .isInstanceOf(RoomNumberNotValidException.class)
                .hasMessageContaining("Room with number " + room + " must have 4 arbitrary characters.");
    }
}