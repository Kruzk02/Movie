package com.app.Expection;

public class CommentNotMatchException extends RuntimeException {
    public CommentNotMatchException(String message) {
        super(message);
    }
}
