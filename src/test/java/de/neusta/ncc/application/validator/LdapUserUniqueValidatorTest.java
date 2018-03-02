package de.neusta.ncc.application.validator;

import de.neusta.ncc.application.validator.exception.LdapUserIsNotUniqueException;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class LdapUserUniqueValidatorTest {

    private final LdapUserUniqueValidator validator = new LdapUserUniqueValidator();

    @Test
    public void validate() {
        validator.validate(Collections.emptyList());
        validator.validate(Collections.singletonList("dreuschling"));
        validator.validate(Arrays.asList("dreuschling", "rsheho"));
        validator.validate(Arrays.asList("dreuschling", "ahaeusler"));
        validator.validate(Arrays.asList("dreuschling", "rsheho", "ahaeusler"));
    }

    @Test
    public void validateWithDuplicates() {
        assertException(Arrays.asList("dreuschling", "dreuschling"));
        assertException(Arrays.asList("dreuschling", "rsheho", "dreuschling"));
        assertException(Arrays.asList("dreuschling", "rsheho", "rsheho", "ahaeusler"));
    }

    private void assertException(List<String> rooms) {
        try {
            validator.validate(rooms);
            fail("Should throw exception");
        } catch (LdapUserIsNotUniqueException e) {
            assertThat(e.getMessage()).isEqualTo("LDAP users should only appear once.");
        }
    }

}