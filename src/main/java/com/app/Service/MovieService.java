package com.app.Service;

import com.app.DTO.MovieDTO;
import com.app.Entity.Movie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieService {
    Flux<Movie> findAll();
    Mono<Movie> findById(Long id);
    Mono<Movie> save(MovieDTO movieDTO);
    Mono<Movie> update(Long id,MovieDTO movieDTO);
    Mono<Void> delete(Long id);
}
