package com.codedidier.paymybuddy.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.codedidier.paymybuddy.dto.NewTransferDto;

/**
 * Controller for Transfer endpoints.
 *
 * <p>
 * Contains method to add/remove currency from user balance. Contains method to
 * create a new transaction and get all transfer for the authenticated user.
 */
public interface TransferController {
    /**
     * this method will add cash to the current user balance.
     *
     * @param theAmount of money to add
     * @param principal the current user
     * @return success page
     */
    String addCash(int theAmount, Principal principal);

    /**
     * this method will remove cash from the current user balance.
     *
     * @param theAmount of money to remove
     * @param principal the current user
     * @return success page
     */
    String removeCash(String theAmount, Principal principal);

    /**
     * This method will create a new transfer for the current user and the given
     * contact.
     *
     * @param transfer  Dto with creditorEmail, amount, description
     * @param principal the current user (debtor)
     * @return success page
     */
    String createTransfer(NewTransferDto transfer, Principal principal);

    /**
     * This method will get all the transfer of the current user.
     *
     * @param principal the current user
     * @return the view for transfer
     */
    String getTransfers(Model model, Principal principal);

    /**
     * This method will create a new transfer for the current user and the given
     * contact.
     *
     * @param newTransfer Dto with creditorEmail, amount, description
     * @param principal   the current user (debtor)
     * @return success page
     */
    String createTransfer(@Valid NewTransferDto newTransfer, Principal principal, BindingResult bindingResult);

    /**
     * this method will remove cash from the current user balance.
     *
     * @param amount    of money to remove
     * @param principal the current user
     * @return success page
     */
    String removeCash(String amount, Principal principal, BindingResult bindingResult);
}
