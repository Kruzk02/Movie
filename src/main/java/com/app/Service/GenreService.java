package com.app.Service;

import com.app.Entity.Genre;
import com.app.Entity.GenreMoviePK;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenreService {
    Flux<Genre> findAll();
    Mono<Genre> findById(Long id);
    Flux<Genre> findGenreByMovieId(Long movieId);
    Mono<GenreMoviePK> save(Long genreId,Long movieId);
}
