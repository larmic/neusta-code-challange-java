package de.neusta.ncc.infrastructure.mapper;

import de.neusta.ncc.domain.Person;
import de.neusta.ncc.domain.PersonAddition;
import de.neusta.ncc.domain.PersonTitle;
import de.neusta.ncc.infrastructure.mapper.exception.CsvPersonNotValidException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CsvPersonToPersonMapperTest {

    private final CsvPersonToPersonMapper mapper = new CsvPersonToPersonMapper();

    @Test
    public void testMapSimple() {
        final Person mappedPerson = mapper.map("Alexander Cole (acole)");

        assertThat(mappedPerson.getTitle()).isNull();
        assertThat(mappedPerson.getFirstName()).isEqualTo("Alexander");
        assertThat(mappedPerson.getAddition()).isNull();
        assertThat(mappedPerson.getLastName()).isEqualTo("Cole");
        assertThat(mappedPerson.getLdapUser()).isEqualTo("acole");
    }

    @Test
    public void testMapFull() {
        final Person mappedPerson = mapper.map("Dr. Alexander James von Cole (acole)");

        assertThat(mappedPerson.getTitle()).isEqualTo(PersonTitle.DR);
        assertThat(mappedPerson.getFirstName()).isEqualTo("Alexander James");
        assertThat(mappedPerson.getAddition()).isEqualTo(PersonAddition.VON);
        assertThat(mappedPerson.getLastName()).isEqualTo("Cole");
        assertThat(mappedPerson.getLdapUser()).isEqualTo("acole");
    }

    @Test
    public void testMapFullWithMultipleWhiteSpaces() {
        final Person mappedPerson = mapper.map("  Dr.  Alexander   James    von     Cole      (acole)   ");

        assertThat(mappedPerson.getTitle()).isEqualTo(PersonTitle.DR);
        assertThat(mappedPerson.getFirstName()).isEqualTo("Alexander James");
        assertThat(mappedPerson.getAddition()).isEqualTo(PersonAddition.VON);
        assertThat(mappedPerson.getLastName()).isEqualTo("Cole");
        assertThat(mappedPerson.getLdapUser()).isEqualTo("acole");
    }

    @Test
    public void testMapSpecialSigns() {
        final Person mappedPerson = mapper.map("Dr. ÄÖÜäöüéèß ÄÖÜäöüéèß von ÄÖÜäöüéèß (ÄÖÜäöüéèß)");

        assertThat(mappedPerson.getTitle()).isEqualTo(PersonTitle.DR);
        assertThat(mappedPerson.getFirstName()).isEqualTo("ÄÖÜäöüéèß ÄÖÜäöüéèß");
        assertThat(mappedPerson.getAddition()).isEqualTo(PersonAddition.VON);
        assertThat(mappedPerson.getLastName()).isEqualTo("ÄÖÜäöüéèß");
        assertThat(mappedPerson.getLdapUser()).isEqualTo("ÄÖÜäöüéèß");
    }

    @Test
    public void testMapSecondFirstName() {
        final Person mappedPerson = mapper.map("Alexander James Cole (acole)");

        assertThat(mappedPerson.getTitle()).isNull();
        assertThat(mappedPerson.getFirstName()).isEqualTo("Alexander James");
        assertThat(mappedPerson.getAddition()).isNull();
        assertThat(mappedPerson.getLastName()).isEqualTo("Cole");
        assertThat(mappedPerson.getLdapUser()).isEqualTo("acole");
    }

    @Test
    public void testMapWithTitle() {
        final Person mappedPerson = mapper.map("Dr. Uwe Svensson (usvens)");
        assertThat(mappedPerson.getTitle()).isEqualTo(PersonTitle.DR);
        assertThat(mappedPerson.getAddition()).isNull();
        assertThat(mappedPerson.getFirstName()).isEqualTo("Uwe");
        assertThat(mappedPerson.getLastName()).isEqualTo("Svensson");
        assertThat(mappedPerson.getLdapUser()).isEqualTo("usvens");
    }

    @Test
    public void testMapWithAdditionals() {
        final Person mappedPerson = mapper.map("Alexander von Cole (acole)");
        assertThat(mappedPerson.getTitle()).isNull();
        assertThat(mappedPerson.getFirstName()).isEqualTo("Alexander");
        assertThat(mappedPerson.getAddition()).isEqualTo(PersonAddition.VON);
        assertThat(mappedPerson.getLastName()).isEqualTo("Cole");
        assertThat(mappedPerson.getLdapUser()).isEqualTo("acole");

        final Person mappedPerson2 = mapper.map("Alexander van Cole (acole)");
        assertThat(mappedPerson2.getTitle()).isNull();
        assertThat(mappedPerson2.getFirstName()).isEqualTo("Alexander");
        assertThat(mappedPerson2.getAddition()).isEqualTo(PersonAddition.VAN);
        assertThat(mappedPerson2.getLastName()).isEqualTo("Cole");
        assertThat(mappedPerson2.getLdapUser()).isEqualTo("acole");

        final Person mappedPerson3 = mapper.map("Alexander de Cole (acole)");
        assertThat(mappedPerson3.getTitle()).isNull();
        assertThat(mappedPerson3.getFirstName()).isEqualTo("Alexander");
        assertThat(mappedPerson3.getAddition()).isEqualTo(PersonAddition.DE);
        assertThat(mappedPerson3.getLastName()).isEqualTo("Cole");
        assertThat(mappedPerson3.getLdapUser()).isEqualTo("acole");
    }

    @Test
    public void testMapWithoutLastNameButWithAdditional() {
        final Person mappedPerson = mapper.map("Alexander von (acole)");
        assertThat(mappedPerson.getTitle()).isNull();
        assertThat(mappedPerson.getFirstName()).isEqualTo("Alexander");
        assertThat(mappedPerson.getAddition()).isNull();
        assertThat(mappedPerson.getLastName()).isEqualTo("von");
        assertThat(mappedPerson.getLdapUser()).isEqualTo("acole");
    }

    @Test
    public void testMapWithLastNameEqualPersonAddition() {
        Person mappedPerson = mapper.map("Alexander von Van (acole)");
        assertThat(mappedPerson.getTitle()).isNull();
        assertThat(mappedPerson.getFirstName()).isEqualTo("Alexander");
        assertThat(mappedPerson.getAddition()).isEqualTo(PersonAddition.VON);
        assertThat(mappedPerson.getLastName()).isEqualTo("Van");
        assertThat(mappedPerson.getLdapUser()).isEqualTo("acole");
    }

    @Test(expected = CsvPersonNotValidException.class)
    public void testMapWithThreeNamesAndNoAddition() {
        Person mappedPerson = mapper.map("Alexander James Cole Pinnhammer (acole)");
        assertThat(mappedPerson.getTitle()).isNull();
        assertThat(mappedPerson.getFirstName()).isEqualTo("Alexander");
        assertThat(mappedPerson.getAddition()).isEqualTo(PersonAddition.VON);
        assertThat(mappedPerson.getLastName()).isEqualTo("Van");
        assertThat(mappedPerson.getLdapUser()).isEqualTo("acole");
    }

    @Test(expected = CsvPersonNotValidException.class)
    public void testMapWithoutFirstName() {
        mapper.map("Pinnhammer (jpinnhammer)");
    }

    @Test(expected = CsvPersonNotValidException.class)
    public void testMapWithoutLastName() {
        mapper.map("Janina (jpinnhammer)");
    }

    @Test(expected = CsvPersonNotValidException.class)
    public void testMapWithoutLdap() {
        mapper.map("Janina von Pinnhammer");
    }

    @Test(expected = CsvPersonNotValidException.class)
    public void testMapWithEmptyPerson() {
        mapper.map("");
    }
}