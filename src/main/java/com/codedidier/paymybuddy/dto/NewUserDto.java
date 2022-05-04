package com.codedidier.paymybuddy.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Dto for user account creation.
 *
 * <p>
 * Used in Registration controller.
 */
public class NewUserDto {

    private final int id = 0;
    private final int balance = 0;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String lastName;
    @NotBlank
    private String firstName;
    @NotBlank
    private String password;
    private String phone;

    private String addressPrefix;

    private String addressNumber;

    private String addressStreet;

    private String zip;

    private String city;

    public NewUserDto() {
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBalance() {
        return balance;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressPrefix() {
        return addressPrefix;
    }

    public void setAddressPrefix(String addressPrefix) {
        this.addressPrefix = addressPrefix;
    }

    public String getAddressNumber() {
        return addressNumber;
    }

    public void setAddressNumber(String addressNumber) {
        this.addressNumber = addressNumber;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "NewUserDto{" + "id=" + id + ", email='" + email + '\'' + ", lastName='" + lastName + '\''
                + ", firstName='" + firstName + '\'' + ", password='" + password + '\'' + ", balance=" + balance
                + ", phone='" + phone + '\'' + ", addressPrefix='" + addressPrefix + '\'' + ", addressNumber='"
                + addressNumber + '\'' + ", addressStreet='" + addressStreet + '\'' + ", zip='" + zip + '\''
                + ", city='" + city + '\'' + '}';
    }
}
