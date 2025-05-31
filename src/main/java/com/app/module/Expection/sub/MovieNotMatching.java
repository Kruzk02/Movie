package com.app.module.Expection.sub;

public class MovieNotMatching extends RuntimeException{
    public MovieNotMatching(String message) {
        super(message);
    }
}
