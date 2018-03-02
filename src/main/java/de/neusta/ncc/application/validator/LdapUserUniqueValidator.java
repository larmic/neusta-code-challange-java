package de.neusta.ncc.application.validator;

import de.neusta.ncc.application.validator.exception.LdapUserIsNotUniqueException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Validate each ldap user is unique in given list of strings (with ignoring case sensitive).
 * Acceptance criteria: A person exists only once in an import file
 */
@Component
public class LdapUserUniqueValidator {

    public void validate(List<String> ldapUsers) throws LdapUserIsNotUniqueException {
        if (!new StringListUniqueChecker().itemsUnique(ldapUsers)) {
            throw new LdapUserIsNotUniqueException();
        }
    }

}
