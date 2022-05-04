package com.codedidier.paymybuddy.dto;

import java.util.Objects;

/** Dto for contact. */
public class ContactDto {

    private String firstName;
    private String lastName;
    private String email;

    public ContactDto() {
    }

    /**
     * Constructor for contact Dto.
     *
     * @param firstName of the contact.
     * @param lastName  of the contact.
     * @param email     of the contact.
     */
    public ContactDto(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContactDto that = (ContactDto) o;
        return Objects.equals(firstName, that.firstName)
                && Objects.equals(lastName, that.lastName)
                && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email);
    }

    @Override
    public String toString() {
        return "ContactDto{" + "firstName='" + firstName + '\'' + ", lastName='" + lastName + '\''
                + ", email='" + email + '\'' + '}';
    }
}
