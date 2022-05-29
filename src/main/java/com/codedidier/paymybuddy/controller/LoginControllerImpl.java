package com.codedidier.paymybuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Implementation for LoginController.
 *
 * <p>
 * Contain method to return the welcome page and the login page.
 */
@Controller
public class LoginControllerImpl implements LoginController {

    /**
     * This method will return the welcome page of the app. This page doesn't need
     * authentication.
     *
     * @return the html page
     */
    @Override
    @GetMapping("/")
    public String welcomePage() {

        return "welcome-page";
    }

    /**
     * This method will return the login page of the app where user can
     * sign-up/sign-in.
     *
     * @return the html page.
     */
    @Override
    @GetMapping("/showLoginPage")
    public String showLoginPage() {

        return "plain-login";
    }
}
