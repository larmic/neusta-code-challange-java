package de.neusta.ncc.domain;

public class Person {

    private final String firstName;
    private final String lastName;
    private final PersonTitle title;
    private final PersonAddition addition;
    private final String ldapUser;

    private Person(String firstName, String lastName, PersonTitle title, PersonAddition addition, String ldapUser) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.addition = addition;
        this.ldapUser = ldapUser;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public PersonTitle getTitle() {
        return title;
    }

    public PersonAddition getAddition() {
        return addition;
    }

    public String getLdapUser() {
        return ldapUser;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();

        if (title != null) {
            stringBuilder.append(title.getLabel()).append(" ");
        }

        stringBuilder.append(firstName).append(" ");

        if (addition != null) {
            stringBuilder.append(addition.getLabel()).append(" ");
        }

        stringBuilder.append(lastName).append(" (").append(ldapUser).append(")");

        return stringBuilder.toString();
    }

    public static class PersonBuilder {
        private final String firstName;
        private final String lastName;
        private final String ldapUser;
        private String secondFirstName;
        private PersonTitle title;
        private PersonAddition addition;

        public PersonBuilder(String firstName, String lastName, String ldapUser) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.ldapUser = ldapUser;
        }

        public PersonBuilder secondFirstName(String secondFirstName) {
            this.secondFirstName = secondFirstName;
            return this;
        }

        public PersonBuilder title(PersonTitle personTitle) {
            this.title = personTitle;
            return this;
        }

        public PersonBuilder addition(PersonAddition personAddition) {
            this.addition = personAddition;
            return this;
        }

        public Person build() {
            if (secondFirstName == null || secondFirstName.trim().isEmpty()) {
                return new Person(firstName.trim(), lastName.trim(), title, addition, ldapUser.trim());
            }
            return new Person(firstName.trim() + " " + secondFirstName.trim(), lastName.trim(), title, addition, ldapUser.trim());
        }
    }
}
