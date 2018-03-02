package de.neusta.ncc.infrastructure.mapper;

import de.neusta.ncc.infrastructure.mapper.exception.CsvPersonNotValidException;
import de.neusta.ncc.domain.Person;
import de.neusta.ncc.domain.PersonAddition;
import de.neusta.ncc.domain.PersonTitle;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CsvPersonToPersonMapper {

    private static final String pattern = "^"
            + "\\s*"
            + "(?<title>(Dr\\.))?"
            + "\\s*"
            + "(?<firstName>\\S+)"
            + "\\s*"
            + "(?<extraName>\\S+)?(?<!von|van|de)"
            + "\\s*"
            + "(?<addition>von|van|de)?"
            + "\\s+"
            + "(?<lastName>\\S+)"
            + "\\s+"
            + "\\((?<ldap>\\S+)\\)"
            + "\\s*"
            + "$";

    private static final Pattern compile = Pattern.compile(pattern);

    public Person map(String csvImportPerson) throws CsvPersonNotValidException {
        final Matcher matcher = compile.matcher(csvImportPerson);
        if (matcher.find()) {
            return new Person.PersonBuilder(matcher.group("firstName"), matcher.group("lastName"), matcher.group("ldap"))
                    .title(PersonTitle.valueOfByLabel(matcher.group("title")))
                    .secondFirstName(matcher.group("extraName"))
                    .addition(PersonAddition.valueOfByLabel(matcher.group("addition")))
                    .build();
        }

        throw new CsvPersonNotValidException(csvImportPerson);
    }

}

