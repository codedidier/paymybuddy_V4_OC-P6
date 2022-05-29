package com.codedidier.paymybuddy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when data already exist in DB with HTTP 409 error. ex:
 * attempt to create duplicate user account.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DataAlreadyExistException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataAlreadyExistException(String message) {
        super(message);
    }
}
