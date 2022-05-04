package com.codedidier.paymybuddy.controller;

import java.security.Principal;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.codedidier.paymybuddy.dto.ContactDto;
import com.codedidier.paymybuddy.exception.BadArgumentException;
import com.codedidier.paymybuddy.service.UserService;

/**
 * Implementation for ContactController.
 *
 * <p>
 * Contain method to add/delete/getAll Contact for the authenticated user.
 */
@Controller
public class ContactControllerImpl implements ContactController {

    private static final Logger log = LogManager.getLogger(ContactControllerImpl.class);
    private final UserService userService;

    @Autowired
    public ContactControllerImpl(UserService userService) {
        this.userService = userService;
    }

    /**
     * This method add a new contact to a user ContactList with the email of its
     * future contact.
     *
     * @param email of the contact to add // * @param principal current user logged
     *              in
     */
    @Override
    @PostMapping("/newContact")
    @ResponseStatus(HttpStatus.CREATED)
    public String addContact(@RequestParam String email, Principal principal) {

        if (email == null || email.isBlank()) {
            log.warn("KO - invalid Email : " + email);
            throw new BadArgumentException("Error - blank email.");
        }
        String userEmail = principal.getName();

        log.info("Saving new contact : " + email + " - for user : " + userEmail);
        userService.addNewContact(email, userEmail);
        return "success";
    }

    /**
     * This method will delete a contact from the ContactList of ths user.
     *
     * @param email     the email of the contact to delete
     * @param principal the current user logged in
     * @return success page
     */
    @Override
    @DeleteMapping("/deleteContact")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String deleteContact(@RequestParam String email, Principal principal) {

        String userEmail = principal.getName();
        log.info("Deleting contact : " + email + " - for user : " + userEmail);
        userService.deleteContact(email, userEmail);
        return "success";
    }

    /**
     * This method return all contact for the current user.
     *
     * @param principal the current user.
     * @return the collection of ContactDto
     */
    @Override
    @GetMapping("/home/contact")
    @ResponseStatus(HttpStatus.OK)
    public String getContact(Principal principal, Model model) {

        String userEmail = principal.getName();
        log.info("Get contact list for user : " + userEmail);
        Collection<ContactDto> contacts = userService.getAllContact(userEmail);
        model.addAttribute("contacts", contacts);
        return "contact-page";
    }
}
