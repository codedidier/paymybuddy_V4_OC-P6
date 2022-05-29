package com.codedidier.paymybuddy.controller;

import java.security.Principal;

import org.springframework.ui.Model;

/**
 * Interface for ContactController.
 *
 * <p>
 * Contains method to add/delete/getAll contact from a user.
 */
public interface ContactController {

    /**
     * This method add a new contact to a user ContactList with the email of its
     * future contact.
     *
     * @param email email of the contact to add //@param principal current user
     *              logged in
     */
    String addContact(String email, Principal principal);

    /**
     * This method will delete a contact from the ContactList of ths user.
     *
     * @param email     the email of the contact to delete
     * @param principal the current user logged in
     * @return success page
     */
    String deleteContact(String email, Principal principal);

    /**
     * This method return all contact for the current user.
     *
     * @param principal the current user.
     * @return the collection of ContactDto
     */
    String getContact(Principal principal, Model model);
}
