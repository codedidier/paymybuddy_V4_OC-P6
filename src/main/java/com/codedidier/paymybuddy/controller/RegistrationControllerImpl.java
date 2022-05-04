package com.codedidier.paymybuddy.controller;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.codedidier.paymybuddy.dto.NewUserDto;
import com.codedidier.paymybuddy.exception.BadArgumentException;
import com.codedidier.paymybuddy.exception.DataAlreadyExistException;
import com.codedidier.paymybuddy.model.UserModel;
import com.codedidier.paymybuddy.service.UserService;

/**
 * Implementation for RegistrationController.
 *
 * <p>
 * Contains method to get and post the register form.
 */

@Controller
@RequestMapping("/registration")
public class RegistrationControllerImpl implements RegistrationController {

    private final UserService userService;
    private final Logger log = LogManager.getLogger(getClass().getName());

    @Autowired
    public RegistrationControllerImpl(UserService userService) {
        this.userService = userService;
    }

    /**
     * This method return the registration form to create a new user.
     *
     * @return html page with registration form
     */
    @Override
    @GetMapping("/signUp")
    public String signUp() {

        log.trace("Returning registration Form.");

        return "registration-page";
    }

    /**
     * This method create a new user in db.
     *
     * @param newUser the new user with all field mandatory (except balance and id).
     * @return html page that confirm registration
     */
    @Override
    @PostMapping("/createNewUser")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerNewUser(@Valid NewUserDto newUser, BindingResult bindingResult) {
        log.trace("New user :" + newUser);
        // Check if there is error in validation
        if (bindingResult.hasErrors()) {
            log.warn(bindingResult.getFieldError());
            throw new BadArgumentException("KO - error in registration form.");
        }

        // Check if email already exist in DB.

        if (userService.findByEmail(newUser.getEmail()).isPresent()) {
            log.error("KO - user: " + newUser.getEmail() + " already exist.");
            throw new DataAlreadyExistException("KO - user: " + newUser.getEmail() + " already exist.");
        }

        UserModel user = new UserModel();
        BeanUtils.copyProperties(newUser, user);
        log.trace("savind user : " + user);
        // Create user
        userService.save(user);

        return "registration-confirmation";
    }
}
