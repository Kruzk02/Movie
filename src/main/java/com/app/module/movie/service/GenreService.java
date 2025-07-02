package com.app.module.movie.service;

import com.app.module.movie.entity.Genre;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenreService {
    Flux<Genre> findAll();
    Mono<Genre> findById(Long id);

}
