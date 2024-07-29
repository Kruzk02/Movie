package com.app.Expection;

public class MovieMediaNotFound extends RuntimeException{
    public MovieMediaNotFound(String message) {
        super(message);
    }
}
