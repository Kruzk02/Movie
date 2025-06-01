package com.app.expection.sub;

public class CommentNotMatchException extends RuntimeException {
    public CommentNotMatchException(String message) {
        super(message);
    }
}
