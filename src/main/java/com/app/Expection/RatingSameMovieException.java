package com.app.Expection;

public class RatingSameMovieException extends RuntimeException {
    public RatingSameMovieException(String message) {
        super(message);
    }
}
