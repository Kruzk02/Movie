package com.app.Service;

import com.app.DTO.GenreMovieDTO;
import com.app.Entity.GenreMoviePK;
import reactor.core.publisher.Mono;

public interface GenreMovieService {
    Mono<GenreMoviePK> save(GenreMovieDTO genreMovieDTO);
}
