package com.codedidier.paymybuddy.service;

import java.util.Collection;
import java.util.Optional;

import com.codedidier.paymybuddy.dto.ContactDto;
import com.codedidier.paymybuddy.entity.User;
import com.codedidier.paymybuddy.model.UserModel;

/**
 * Interface for UserService. Contains method used by CRUD controller.
 */
public interface UserService {
    /**
     * This method save the given user by calling the repository.
     *
     * @param theUser to save
     */
    void save(UserModel theUser);

    /**
     * This method update the user account by calling repository.
     *
     * @param theUser to update
     */
    void update(UserModel theUser);

    /**
     * This method will delete an user from database with its id.
     *
     * @param theId of the user to delete
     */
    void deleteUserById(int theId);

    /**
     * This method will return an user from repository based on its id.
     *
     * @param theId of the user retrieve
     * @return the user
     */
    Optional<User> findById(int theId);

    /**
     * This method will return an user from repository based on its email.
     *
     * @param email of the user to retrieve
     * @return the user
     */
    Optional<User> findByEmail(String email);

    /**
     * This method will add a new contact to the contact list of the current user
     * using repository.
     *
     * @param contactEmail the email of the contact to add
     * @param userEmail    the email of the current user
     */
    void addNewContact(String contactEmail, String userEmail);

    /**
     * This method will delete the given contact from the contact list of the
     * current user.
     *
     * @param email     the email of the contact to delete from list
     * @param userEmail the email of the current user
     */
    void deleteContact(String email, String userEmail);

    /**
     * This method returns a collection of all the contacts of the given user.
     *
     * @param userEmail the user email.
     * @return all the contacts
     */
    Collection<ContactDto> getAllContact(String userEmail);
}
