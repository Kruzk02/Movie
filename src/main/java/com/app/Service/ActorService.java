package com.app.Service;

import com.app.DTO.ActorDTO;
import com.app.Entity.Actor;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ActorService {
    Flux<Actor> findAll();
    Mono<Actor> findById(Long id);
    Mono<Actor> save(ActorDTO actorDTO);
    Mono<Actor> update(Long id,ActorDTO actorDTO);
    Mono<Void> delete(Long id);
}
