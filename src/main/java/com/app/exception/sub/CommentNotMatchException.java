package com.app.exception.sub;

public class CommentNotMatchException extends RuntimeException {
    public CommentNotMatchException(String message) {
        super(message);
    }
}
