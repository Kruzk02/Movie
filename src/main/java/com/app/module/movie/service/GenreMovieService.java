package com.app.module.movie.service;

import com.app.module.movie.dto.GenreDTO;
import com.app.module.movie.entity.Genre;
import com.app.module.movie.entity.Movie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenreMovieService {
    Flux<Genre> findGenreByMovieId(Long movieId);
    Mono<Void> saveGenreMovie(GenreDTO genreDTO);
    Mono<Void> updateGenreMovie(GenreDTO genreDTO);
}
