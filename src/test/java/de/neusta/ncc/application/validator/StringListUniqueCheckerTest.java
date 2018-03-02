package de.neusta.ncc.application.validator;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class StringListUniqueCheckerTest {

    private final StringListUniqueChecker validator = new StringListUniqueChecker();

    @Test
    public void testValidate() {
        assertThat(validator.itemsUnique(Arrays.asList("1243", "ÄÖÜ?", "1222"))).isTrue();
    }

    @Test
    public void testValidateWithSimpleDuplicates() {
        assertThat(validator.itemsUnique(Arrays.asList("1243", "ÄÖÜ?", "1222", "1222"))).isFalse();
        assertThat(validator.itemsUnique(Arrays.asList("1243", "ÄÖÜ?", "1222", "1222"))).isFalse();
        assertThat(validator.itemsUnique(Arrays.asList("1243", "1243", "ÄÖÜ?", "1222"))).isFalse();
    }

    @Test
    public void testValidateWithFailedCaseInsensitive() {
        assertThat(validator.itemsUnique(Arrays.asList("äÖÜ?", "ÄÖÜ?"))).isFalse();
        assertThat(validator.itemsUnique(Arrays.asList("äÖÜ?", "ÄÖÜ?"))).isFalse();
        assertThat(validator.itemsUnique(Arrays.asList("ZZZZ", "zzZz"))).isFalse();
    }

    @Test
    public void testValidateWithNumbersAreEmpty() {
        assertThat(validator.itemsUnique(Collections.emptyList())).isTrue();
    }
}