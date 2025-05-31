package com.app.module.genre.service;

import com.app.module.genre.entity.Genre;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenreService {
    Flux<Genre> findAll();
    Mono<Genre> findById(Long id);

}
