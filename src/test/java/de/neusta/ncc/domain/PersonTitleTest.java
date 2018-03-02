package de.neusta.ncc.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfByLabelWithLabelIsUnknown() {
        PersonTitle.valueOfByLabel("not-known");
    }
}