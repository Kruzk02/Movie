package com.app.exception.sub;

public class RatingSameMovieException extends RuntimeException {
    public RatingSameMovieException(String message) {
        super(message);
    }
}
