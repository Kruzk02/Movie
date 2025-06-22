package com.app.module.genre.service;

import com.app.module.genre.dto.GenreDTO;
import com.app.module.genre.entity.Genre;
import com.app.module.genre.entity.Movie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenreMovieService {
    Flux<Genre> findGenreByMovieId(Long movieId);
    Mono<Void> saveGenreMovie(GenreDTO genreDTO);
    Mono<Void> updateGenreMovie(GenreDTO genreDTO);
    Flux<Movie> findMovieByGenreId(Long id);
}
