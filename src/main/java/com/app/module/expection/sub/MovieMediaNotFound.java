package com.app.module.expection.sub;

public class MovieMediaNotFound extends RuntimeException{
    public MovieMediaNotFound(String message) {
        super(message);
    }
}
