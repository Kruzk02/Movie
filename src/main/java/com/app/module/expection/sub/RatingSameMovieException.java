package com.app.module.expection.sub;

public class RatingSameMovieException extends RuntimeException {
    public RatingSameMovieException(String message) {
        super(message);
    }
}
