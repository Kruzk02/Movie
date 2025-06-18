package com.app.Service;

import com.app.DTO.ActorMovieDTO;
import com.app.Entity.Actor;
import module.movie.entity.Movie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ActorMovieService {
    Mono<Void> saveActorMovie(ActorMovieDTO actorMovieDTO);
    Mono<Void> updateActorMovie(ActorMovieDTO actorMovieDTO);
    Flux<Actor> findActorByMovieId(Long movieId);
    Flux<Movie> findMovieByActor(Long id);
}
