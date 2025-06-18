package com.app.module.director.service;

import com.app.module.director.dto.DirectorMovieDTO;
import com.app.module.director.entity.Director;
import com.app.module.movie.entity.Movie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DirectorMovieService {
    Mono<Void> saveDirectorMovie(DirectorMovieDTO directorMovieDTO);
    Mono<Void> updateDirectorMovie(DirectorMovieDTO directorMovieDTO);
    Flux<Director> findDirectorByMovieId(Long movieId);
    Flux<Movie> findMovieByDirector(Long id);
}
