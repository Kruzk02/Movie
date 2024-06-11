package com.app.Service;

import com.app.Entity.Genre;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenreService {
    Flux<Genre> findAll();
    Mono<Genre> findById(Long id);

}
