package com.app.expection.sub;

public class RatingSameMovieException extends RuntimeException {
    public RatingSameMovieException(String message) {
        super(message);
    }
}
