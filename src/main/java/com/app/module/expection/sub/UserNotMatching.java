package com.app.module.expection.sub;

public class UserNotMatching extends RuntimeException{
    public UserNotMatching(String message) {
        super(message);
    }
}
