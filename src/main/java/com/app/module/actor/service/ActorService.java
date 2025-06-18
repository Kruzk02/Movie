package com.app.module.actor.service;

import com.app.module.actor.dto.ActorDTO;
import com.app.module.actor.entity.Actor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ActorService {
    Flux<Actor> findAll();
    Mono<Actor> findById(Long id);
    Mono<Actor> save(ActorDTO actorDTO);
    Mono<Actor> update(Long id,ActorDTO actorDTO);
    Mono<Void> delete(Long id);
}
