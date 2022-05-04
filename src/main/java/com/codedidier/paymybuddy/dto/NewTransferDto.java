package com.codedidier.paymybuddy.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * Dto of transfer creation.
 *
 * <p>
 * Used in transfer controller.
 */
public class NewTransferDto {

    @NotBlank
    private String creditorEmail;

    private String debtorEmail;

    @Min(0)
    private double amount;

    private int charge;

    private String description;

    /**
     * Constructor with parameters.
     *
     * @param creditorEmail email of the contact who get the money.
     * @param amount        the amount of money with 2 decimals.
     * @param description   small description.
     */
    public NewTransferDto(String creditorEmail, double amount, String description) {
        this.creditorEmail = creditorEmail;
        this.amount = amount;
        this.description = description;
    }

    public NewTransferDto() {
    }

    public String getCreditorEmail() {
        return creditorEmail;
    }

    public void setCreditorEmail(String creditorEmail) {
        this.creditorEmail = creditorEmail;
    }

    public String getDebtorEmail() {
        return debtorEmail;
    }

    public void setDebtorEmail(String debtorEmail) {
        this.debtorEmail = debtorEmail;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    @Override
    public String toString() {
        return "NewTransferDto{" + "creditorEmail='" + creditorEmail + '\'' + ", debtorEmail='" + debtorEmail
                + '\'' + ", amount=" + amount + ", description='" + description + '\'' + '}';
    }
}
