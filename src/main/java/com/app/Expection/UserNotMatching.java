package com.app.Expection;

public class UserNotMatching extends RuntimeException{
    public UserNotMatching(String message) {
        super(message);
    }
}
