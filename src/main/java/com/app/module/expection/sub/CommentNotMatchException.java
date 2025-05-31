package com.app.module.expection.sub;

public class CommentNotMatchException extends RuntimeException {
    public CommentNotMatchException(String message) {
        super(message);
    }
}
