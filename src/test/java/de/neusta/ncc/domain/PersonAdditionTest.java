package de.neusta.ncc.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonAdditionTest {

    @Test
    public void testValueOfByLabel() {
        assertThat(PersonAddition.valueOfByLabel("von")).isEqualTo(PersonAddition.VON);
        assertThat(PersonAddition.valueOfByLabel(" von ")).isEqualTo(PersonAddition.VON);
        assertThat(PersonAddition.valueOfByLabel("van")).isEqualTo(PersonAddition.VAN);
        assertThat(PersonAddition.valueOfByLabel(" van ")).isEqualTo(PersonAddition.VAN);
        assertThat(PersonAddition.valueOfByLabel("de")).isEqualTo(PersonAddition.DE);
        assertThat(PersonAddition.valueOfByLabel(" de ")).isEqualTo(PersonAddition.DE);
    }

    @Test
    public void testValueOfByLabelWithLabelIsEmpty() {
        assertThat(PersonAddition.valueOfByLabel("")).isNull();
        assertThat(PersonAddition.valueOfByLabel(" ")).isNull();
    }

    @Test
    public void testValueOfByLabelWithLabelIsNull() {
        assertThat(PersonAddition.valueOfByLabel(null)).isNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfByLabelWithLabelIsUnknown() {
        PersonAddition.valueOfByLabel("not-known");
    }
}