package com.codedidier.paymybuddy.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/** Dto for transfer controller. */
public class GetTransferDto {

    private String contactName;
    private String description;
    private BigDecimal amount;

    public GetTransferDto() {
    }

    /**
     * Constructor with all parameters.
     *
     * @param contactName the contact name.
     * @param description small description of the transfer.
     * @param amount      the amount transferred with 2 decimals
     */
    public GetTransferDto(String contactName, String description, BigDecimal amount) {
        this.contactName = contactName;
        this.description = description;
        this.amount = amount;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount.setScale(2, RoundingMode.HALF_DOWN);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GetTransferDto that = (GetTransferDto) o;
        return Objects.equals(contactName, that.contactName)
                && Objects.equals(description, that.description)
                && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contactName, description, amount);
    }

    @Override
    public String toString() {
        return "GetTransferDto{" + "contactName='" + contactName + '\'' + ", description='" + description
                + '\'' + ", amount=" + amount + '}';
    }
}
