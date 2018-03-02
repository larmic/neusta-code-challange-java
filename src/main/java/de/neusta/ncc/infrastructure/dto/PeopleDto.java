package de.neusta.ncc.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * People dto to wrap inner data model from json representation,
 */
@JsonPropertyOrder({"title", "firstName", "addition", "lastName", "ldapUser"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "PeopleDto")
public class PeopleDto {

    @ApiModelProperty(value = "first name", required = true, example = "Alexander James")
    private String firstName;

    @ApiModelProperty(value = "Last name", required = true, example = "Cole")
    private String lastName;

    @ApiModelProperty(value = "Title", example = "Dr.")
    private String title;

    @ApiModelProperty(value = "Name addition", example = "von|van|de")
    private String addition;

    @ApiModelProperty(value = "Unique ldap name", required = true, example = "acole")
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
