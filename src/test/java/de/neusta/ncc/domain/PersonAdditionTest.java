package de.neusta.ncc.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    public void testValueOfByLabelWithLabelIsUnknown() {
        assertThatThrownBy(() -> PersonAddition.valueOfByLabel("not-known"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Person addition not-known is not supported");
    }
}