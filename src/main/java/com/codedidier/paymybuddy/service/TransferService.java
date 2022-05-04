package com.codedidier.paymybuddy.service;

import java.util.List;

import com.codedidier.paymybuddy.dto.GetTransferDto;
import com.codedidier.paymybuddy.dto.NewTransferDto;

/**
 * Interface for transfer service. Contain some method to add/remove currency
 * from user balance.
 */
public interface TransferService {
    /**
     * This method will add some cash to User balance.
     *
     * @param amount the integer to add
     * @param email  the email of the current user
     */
    void addCash(int amount, String email);

    /**
     * This method will remove some cash to User balance.
     *
     * @param amount the integer to subtract
     * @param email  the email of the current user
     */
    void removeCash(String amount, String email);

    /**
     * This method will create a new Transfer between two user.
     *
     * @param newTransferDto dto with creditorEmail, debtorEmail, amount and
     *                       description
     */
    GetTransferDto createTransfer(NewTransferDto newTransferDto);

    /**
     * This method will get all transfer for the given user.
     *
     * @param userEmail the email of the current user.
     */
    List<GetTransferDto> getTransfers(String userEmail);
}
