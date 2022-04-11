package de.neusta.ncc.domain;

import de.neusta.ncc.infrastructure.mapper.exception.CsvPersonNotValidException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PersonTitleTest {

    @Test
    public void testValueOfByLabel() {
        assertThat(PersonTitle.valueOfByLabel("Dr.")).isEqualTo(PersonTitle.DR);
        assertThat(PersonTitle.valueOfByLabel(" Dr. ")).isEqualTo(PersonTitle.DR);
    }

    @Test
    public void testValueOfByLabelWithLabelIsEmpty() {
        assertThat(PersonTitle.valueOfByLabel("")).isNull();
        assertThat(PersonTitle.valueOfByLabel(" ")).isNull();
    }

    @Test
    public void testValueOfByLabelWithLabelIsNull() {
        assertThat(PersonTitle.valueOfByLabel(null)).isNull();
    }

    @Test
    public void testValueOfByLabelWithLabelIsUnknown() {
        assertThatThrownBy(() -> PersonTitle.valueOfByLabel("not-known"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Person title not-known is not supported");
    }
}