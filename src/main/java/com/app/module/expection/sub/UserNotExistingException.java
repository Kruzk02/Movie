package com.app.module.expection.sub;

public class UserNotExistingException extends RuntimeException{
    public UserNotExistingException(String message) {
        super(message);
    }
}
