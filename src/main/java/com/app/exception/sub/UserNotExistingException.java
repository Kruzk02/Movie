package com.app.exception.sub;

public class UserNotExistingException extends RuntimeException{
    public UserNotExistingException(String message) {
        super(message);
    }
}
