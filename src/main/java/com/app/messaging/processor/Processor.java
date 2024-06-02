package com.app.messaging.processor;

import com.app.Entity.Movie;
import reactor.core.publisher.Mono;

public interface Processor<E> {
    void processMovie(E e);
    Mono<E> getMovie();
}
