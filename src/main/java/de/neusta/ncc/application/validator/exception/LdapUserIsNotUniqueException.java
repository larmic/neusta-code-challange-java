package de.neusta.ncc.application.validator.exception;

public class LdapUserIsNotUniqueException extends RuntimeException {

    public LdapUserIsNotUniqueException() {
        super("LDAP users should only appear once.", null);
    }

}
