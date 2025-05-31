package com.app.module.Expection.sub;

public class UserNotMatching extends RuntimeException{
    public UserNotMatching(String message) {
        super(message);
    }
}
