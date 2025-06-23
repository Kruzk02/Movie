package com.app.exception.sub;

public class UserNotMatching extends RuntimeException{
    public UserNotMatching(String message) {
        super(message);
    }
}
