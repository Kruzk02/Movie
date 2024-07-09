package com.app.Expection;

public class UserNotExistingException extends RuntimeException{
    public UserNotExistingException(String message) {
        super(message);
    }
}
