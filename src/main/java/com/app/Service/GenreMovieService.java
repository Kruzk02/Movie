package com.app.Service;

import com.app.DTO.GenreDTO;
import com.app.Entity.Genre;
import module.movie.entity.Movie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenreMovieService {
    Flux<Genre> findGenreByMovieId(Long movieId);
    Mono<Void> saveGenreMovie(GenreDTO genreDTO);
    Mono<Void> updateGenreMovie(GenreDTO genreDTO);
    Flux<Movie> findMovieByGenreId(Long id);
}
