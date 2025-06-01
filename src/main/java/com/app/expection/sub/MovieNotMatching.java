package com.app.expection.sub;

public class MovieNotMatching extends RuntimeException{
    public MovieNotMatching(String message) {
        super(message);
    }
}
