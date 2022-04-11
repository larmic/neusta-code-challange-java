package de.neusta.ncc.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonBuilderTest {

    @Test
    public void testMinimalParameters() {
        final Person person = new Person.PersonBuilder("Uwe", "Svensson", "usvens").build();
        assertThat(person.getFirstName()).isEqualTo("Uwe");
        assertThat(person.getLastName()).isEqualTo("Svensson");
        assertThat(person.getLdapUser()).isEqualTo("usvens");
    }

    @Test
    public void testAddition() {
        final Person person1 = new Person.PersonBuilder("Uwe", "Svensson", "usvens")
                .addition(PersonAddition.VAN)
                .build();
        assertThat(person1.getAddition()).isEqualTo(PersonAddition.VAN);

        final Person person2 = new Person.PersonBuilder("Uwe", "Svensson", "usvens")
                .addition(PersonAddition.DE)
                .build();
        assertThat(person2.getAddition()).isEqualTo(PersonAddition.DE);

        final Person person3 = new Person.PersonBuilder("Uwe", "Svensson", "usvens")
                .addition(PersonAddition.VON)
                .build();
        assertThat(person3.getAddition()).isEqualTo(PersonAddition.VON);
    }

    @Test
    public void testTitle() {
        final Person person = new Person.PersonBuilder("Uwe", "Svensson", "usvens")
                .title(PersonTitle.DR)
                .build();
        assertThat(person.getTitle()).isEqualTo(PersonTitle.DR);
    }

    @Test
    public void testSecondFirstName() {
        final Person person = new Person.PersonBuilder("Uwe", "Svensson", "usvens")
                .secondFirstName("Tom")
                .build();
        assertThat(person.getFirstName()).isEqualTo("Uwe Tom");
    }

    @Test
    public void testSecondFirstNameWithNameIsNull() {
        final Person person = new Person.PersonBuilder("Uwe", "Svensson", "usvens")
                .secondFirstName(null)
                .build();
        assertThat(person.getFirstName()).isEqualTo("Uwe");
    }

    @Test
    public void testSecondFirstNameWithNameIsEmpty() {
        final Person person = new Person.PersonBuilder("Uwe", "Svensson", "usvens")
                .secondFirstName("")
                .build();
        assertThat(person.getFirstName()).isEqualTo("Uwe");
    }

    @Test
    public void testSecondFirstNameWithNameIsEmptySpace() {
        final Person person = new Person.PersonBuilder("Uwe", "Svensson", "usvens")
                .secondFirstName(" ")
                .build();
        assertThat(person.getFirstName()).isEqualTo("Uwe");
    }
}