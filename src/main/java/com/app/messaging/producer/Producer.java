package com.app.messaging.producer;

public interface Producer <E>{
    void send(E e);
}
