package com.app.module.Expection.sub;

public class RatingSameMovieException extends RuntimeException {
    public RatingSameMovieException(String message) {
        super(message);
    }
}
