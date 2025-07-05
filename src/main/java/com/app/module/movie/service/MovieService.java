package com.app.module.movie.service;

import com.app.module.movie.dto.MovieDTO;
import com.app.module.movie.entity.Movie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieService {
    Flux<Movie> findAll();
    Flux<Movie> findMovieByActorId(Long id);
    Flux<Movie> findMovieByDirectorId(Long id);
    Mono<Movie> findById(Long id);
    Mono<Movie> findByIdAndReceiveUserId(Long id,Long userId);
    Mono<Movie> save(MovieDTO movieDTO);
    Mono<Movie> update(Long id,MovieDTO movieDTO);
    Mono<Void> delete(Long id);
}
