package com.codedidier.paymybuddy.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.codedidier.paymybuddy.dto.ContactDto;
import com.codedidier.paymybuddy.entity.User;
import com.codedidier.paymybuddy.exception.DataAlreadyExistException;
import com.codedidier.paymybuddy.exception.DataNotFindException;
import com.codedidier.paymybuddy.model.UserModel;
import com.codedidier.paymybuddy.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation for UserService. Contains method to create/delete/read/update
 * user and contact.
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * This method save the given user in DB by calling Dao. this method is used to
     * create a new user or update an existing user
     *
     * @param theUser to save
     */
    @Override
    public void save(UserModel theUser) {
        log.info("Saving user : " + theUser);
        User user = new User();
        BeanUtils.copyProperties(theUser, user);

        // Hash password using BCrypt
        if (theUser.getPassword() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(theUser.getPassword()));
        }
        // Save user using JpaRepository.save
        userRepository.save(user);
        log.info("Successfully saved user.");
    }

    /**
     * This method update the user account by calling repository.
     *
     * @param theUser to update
     */
    @Override
    public void update(UserModel theUser) {
        log.info("Updating user : " + theUser.getEmail());
        if (!userRepository.existsById(theUser.getId())) {
            log.warn("ERROR DataNotFindException - Can't find account for user: " + theUser.getEmail());
            throw new DataNotFindException("Can't find account for user: " + theUser.getEmail());
        }
        // Copy non null field from user model to user entity
        User userEntity = userRepository.getById(theUser.getId());

        if (theUser.getFirstName() != null && !theUser.getFirstName().isBlank()) {
            log.trace("Updating : firstName");
            userEntity.setFirstName(theUser.getFirstName());
        }

        if (theUser.getLastName() != null && !theUser.getLastName().isBlank()) {
            log.trace("Updating : lastName");
            userEntity.setLastName(theUser.getLastName());
        }

        if (theUser.getEmail() != null && !theUser.getEmail().isBlank()) {
            log.trace("Updating : email");
            userEntity.setEmail(theUser.getEmail());
        }

        if (theUser.getPassword() != null && !theUser.getPassword().isBlank()) {
            log.trace("Updating : password");
            userEntity.setPassword(bCryptPasswordEncoder.encode(theUser.getPassword()));
        }
        if (theUser.getPhone() != null && !theUser.getPhone().isBlank()) {
            log.trace("Updating : phone");
            userEntity.setPhone(theUser.getPhone());
        }

        if (theUser.getAddressPrefix() != null && !theUser.getAddressPrefix().isBlank()) {
            log.trace("Updating : addressPrefix");
            userEntity.setAddressPrefix(theUser.getAddressPrefix());
        }
        if (theUser.getAddressStreet() != null && !theUser.getAddressStreet().isBlank()) {
            log.trace("Updating : addressStreet");
            userEntity.setAddressStreet(theUser.getAddressStreet());
        }
        if (theUser.getAddressStreet() != null && !theUser.getAddressNumber().isBlank()) {
            log.trace("Updating : addressNumber");
            userEntity.setAddressNumber(theUser.getAddressNumber());
        }
        if (theUser.getCity() != null && !theUser.getCity().isBlank()) {
            log.trace("Updating : city");
            userEntity.setCity(theUser.getCity());
        }

        if (theUser.getZip() != null && !theUser.getZip().isBlank()) {
            log.trace("Updating : zip");
            userEntity.setZip(theUser.getZip());
        }

        userRepository.save(userEntity);
        log.info("Successfully updated user.");
    }

    /**
     * This method will delete an user from database with its id.
     *
     * @param theId of the user to delete
     */
    @Override
    public void deleteUserById(int theId) {
        log.info("Deleting user with id : " + theId);
        // Check if user exist
        if (!userRepository.existsById(theId)) {
            log.warn("ERROR DataNotFindException - can't find user with id: " + theId);
            throw new DataNotFindException("KO - can't find user with id: " + theId);
        }
        // Delete user
        userRepository.deleteById(theId);
        log.info("Successfully deleted user.");
    }

    /**
     * This method will return an user from repository based on its id.
     *
     * @param theId of the user retrieve
     * @return the user
     */
    @Override
    public Optional<User> findById(int theId) {
        return userRepository.findById(theId);
    }

    /**
     * This method will return an user from repository based on its email.
     *
     * @param email of the user to retrieve
     * @return the user
     */
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * This method will add a new contact to the contact list of the current user
     * using repository.
     *
     * @param contactEmail the email of the contact to add
     * @param userEmail    the email of the current user
     */
    @Override
    public void addNewContact(String contactEmail, String userEmail) {

        log.info("Saving contact : " + contactEmail + " - user : " + userEmail);

        // check if user for contactEmail exist
        Optional<User> contactOp = userRepository.findByEmail(contactEmail);
        if (contactOp.isEmpty()) {
            log.warn("ERROR DataNotFindException - can't find user: " + contactEmail);
            throw new DataNotFindException("KO - can't find user: " + contactEmail);
        }
        User contact = contactOp.get();

        // check if contact already exist in userContactList
        User user = userRepository.findByEmail(userEmail).get();
        if (user.getContacts().contains(contact)) {
            log.warn(
                    "ERROR DataAlreadyExistException - This contact is already linked, contact: "
                            + contactEmail);
            throw new DataAlreadyExistException(
                    "KO - This contact is already linked, contact: " + contactEmail);
        }

        // save and return firstName and lastName of contact
        user.getContacts().add(contact);
        userRepository.save(user);
        log.info("Successfully added contact.");
    }

    /**
     * This method will delete the given contact from the contact list of the
     * current user
     *
     * @param email     the email of the contact to delete from list
     * @param userEmail the email of the current user
     */
    @Override
    public void deleteContact(String email, String userEmail) {
        log.info("Deleting contact : " + email + " - user : " + userEmail);
        // get current user
        User user = userRepository.findByEmail(userEmail).get();

        // check if contact is present in contactList
        Set<User> contactList = user.getContacts();
        User userToDelete = new User();
        boolean deleted = false;
        for (User contact : contactList) {
            if (Objects.equals(contact.getEmail(), email)) {
                // create dto of contact for return

                deleted = true;
                // remove contact from list
                userToDelete = contact;
            }
        }
        if (!deleted) {
            log.warn("ERROR DataNotFindException - can't find user with email: " + email);
            throw new DataNotFindException("Error - can't find user with email: " + email);
        }
        contactList.remove(userToDelete);
        // save
        userRepository.save(user);
        log.info("Successfully removed contact.");
    }

    /**
     * This method returns a collection of all the contacts of the given user.
     *
     * @param userEmail the user email.
     * @return all the contacts
     */
    @Override
    public Collection<ContactDto> getAllContact(String userEmail) {
        log.info("Getting all contact for user : " + userEmail);
        // Get User from repo
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        User user = new User();
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }

        // Get contact List
        Set<User> contactList = user.getContacts();

        // Map every contact to its ContactDto
        Collection<ContactDto> dtoCollection = new ArrayList<>();
        for (User contact : contactList) {
            ContactDto temp = new ContactDto(contact.getFirstName(), contact.getLastName(), contact.getEmail());
            dtoCollection.add(temp);
        }
        log.info("Returning " + dtoCollection.size() + " contacts.");
        // return a collection of ContactDto
        return dtoCollection;
    }
}
