package com.app.module.Expection.sub;

public class CommentNotMatchException extends RuntimeException {
    public CommentNotMatchException(String message) {
        super(message);
    }
}
