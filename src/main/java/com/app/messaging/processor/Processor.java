package com.app.messaging.processor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Processor<E> {
    void process(E e);
    Mono<E> getOneData();
    Flux<E> getMultipleData();
}
