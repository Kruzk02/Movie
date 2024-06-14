package com.app.Service;

import com.app.DTO.DirectorMovieDTO;
import com.app.Entity.Director;
import com.app.Entity.Movie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DirectorMovieService {
    Mono<Void> saveDirectorMovie(DirectorMovieDTO directorMovieDTO);
    Mono<Void> updateDirectorMovie(DirectorMovieDTO directorMovieDTO);
    Flux<Director> findDirectorByMovieId(Long movieId);
    Flux<Movie> findMovieByDirector(Long id);
}
