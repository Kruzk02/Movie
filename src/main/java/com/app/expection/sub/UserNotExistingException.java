package com.app.expection.sub;

public class UserNotExistingException extends RuntimeException{
    public UserNotExistingException(String message) {
        super(message);
    }
}
