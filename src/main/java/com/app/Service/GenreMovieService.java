package com.app.Service;

import com.app.DTO.GenreDTO;
import com.app.Entity.Genre;
import com.app.Entity.GenreMoviePK;
import com.app.Entity.Movie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenreMovieService {
    Flux<Genre> findGenreByMovieId(Long movieId);
    Mono<GenreMoviePK> save(GenreDTO genreDTO);
    Flux<Movie> findMovieByGenreId(Long id);
}
