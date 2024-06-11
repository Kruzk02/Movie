package com.app.Service;

import com.app.DTO.ActorMovieDTO;
import com.app.Entity.Actor;
import com.app.Entity.ActorMoviePK;
import com.app.Entity.Movie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ActorMovieService {
    Mono<ActorMoviePK> saveActorMovie(ActorMovieDTO actorMovieDTO);
    Flux<Actor> findActorByMovieId(Long movieId);
    Flux<Movie> findMovieByActor(Long id);
}
