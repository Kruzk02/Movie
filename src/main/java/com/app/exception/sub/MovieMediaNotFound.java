package com.app.exception.sub;

public class MovieMediaNotFound extends RuntimeException{
    public MovieMediaNotFound(String message) {
        super(message);
    }
}
