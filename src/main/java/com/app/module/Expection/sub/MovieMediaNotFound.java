package com.app.module.Expection.sub;

public class MovieMediaNotFound extends RuntimeException{
    public MovieMediaNotFound(String message) {
        super(message);
    }
}
