package com.app.module.actor.service;

import com.app.module.actor.dto.ActorMovieDTO;
import com.app.module.actor.entity.Actor;
import com.app.module.actor.entity.Movie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ActorMovieService {
    Mono<Void> saveActorMovie(ActorMovieDTO actorMovieDTO);
    Mono<Void> updateActorMovie(ActorMovieDTO actorMovieDTO);
    Flux<Actor> findActorByMovieId(Long movieId);
}
