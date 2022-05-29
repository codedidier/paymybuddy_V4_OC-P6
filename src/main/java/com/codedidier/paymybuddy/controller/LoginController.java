package com.codedidier.paymybuddy.controller;

/**
 * Interface for LoginController.
 *
 * <p>
 * Contain method to return the welcome page and the login page.
 */
public interface LoginController {

    /**
     * This method will return the welcome page of the app. This page doesn't need
     * authentication.
     *
     * @return the html page
     */
    String welcomePage();

    /**
     * This method will return the login page of the app where user can
     * sign-up/sign-in.
     *
     * @return the html page.
     */
    String showLoginPage();
}
