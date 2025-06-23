package com.app.exception.sub;

public class MovieNotMatching extends RuntimeException{
    public MovieNotMatching(String message) {
        super(message);
    }
}
