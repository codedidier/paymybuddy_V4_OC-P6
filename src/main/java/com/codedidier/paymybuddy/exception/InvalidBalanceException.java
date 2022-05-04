package com.codedidier.paymybuddy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when process will make balance invalid with HTTP 400 error.
 * ex: action that make balance negative.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidBalanceException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public InvalidBalanceException(String message) {

        super(message);
    }
}
