package com.app.Service;

import com.app.DTO.DirectorMovieDTO;
import com.app.Entity.Director;
import com.app.Entity.DirectorMoviePK;
import com.app.Entity.Movie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DirectorMovieService {
    Mono<DirectorMoviePK> saveDirectorMovie(DirectorMovieDTO directorMovieDTO);
    Flux<Director> findDirectorByMovieId(Long movieId);
    Flux<Movie> findMovieByDirector(Long id);
}
