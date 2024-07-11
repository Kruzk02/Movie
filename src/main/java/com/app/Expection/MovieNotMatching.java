package com.app.Expection;

public class MovieNotMatching extends RuntimeException{
    public MovieNotMatching(String message) {
        super(message);
    }
}
