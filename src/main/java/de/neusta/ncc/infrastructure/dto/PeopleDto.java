package de.neusta.ncc.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * People dto to wrap inner data model from json representation,
 */
@JsonPropertyOrder({"title", "firstName", "addition", "lastName", "ldapUser"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PeopleDto {

    private String firstName;
    private String lastName;
    private String title;
    private String addition;
    private String ldapUser;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public String getLdapUser() {
        return ldapUser;
    }

    public void setLdapUser(String ldapUser) {
        this.ldapUser = ldapUser;
    }
}
