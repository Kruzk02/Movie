package com.app.messaging;

import reactor.core.publisher.Mono;

public interface MessagingService<T>{
    void sendMessage(T messaging);
    Mono<T> receiveMessage();
}
