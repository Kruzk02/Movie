package com.app.module.actor.service.impl;

import com.app.module.actor.dto.ActorMovieDTO;
import com.app.module.actor.entity.Actor;
import com.app.module.actor.entity.ActorMoviePK;
import com.app.messaging.event.EventType;
import com.app.exception.sub.MovieEventException;
import com.app.module.actor.entity.Movie;
import com.app.module.actor.repository.ActorMovieRepository;
import com.app.module.actor.service.ActorMovieService;
import com.app.messaging.consumer.MovieEventConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ActorMovieServiceImpl implements ActorMovieService {

    private final ActorMovieRepository actorMovieRepository;
    private final MovieEventConsumer movieEventConsumer;

    @Autowired
    public ActorMovieServiceImpl(ActorMovieRepository actorMovieRepository, MovieEventConsumer movieEventConsumer) {
        this.actorMovieRepository = actorMovieRepository;
        this.movieEventConsumer = movieEventConsumer;
    }

    @Override
    public Mono<Void> saveActorMovie(ActorMovieDTO actorMovieDTO) {
        return Mono.just(movieEventConsumer.consumerForActor())
            .flatMapMany(movieEvent -> {
                if (movieEvent.getEventType() == EventType.CREATED) {
                    Set<ActorMoviePK> actorMovies = actorMovieDTO.getActorId().stream()
                            .map(actorId -> new ActorMoviePK(actorId,movieEvent.getMovieId()))
                            .collect(Collectors.toSet());
                    return actorMovieRepository.saveAll(actorMovies);
                } else {
                    throw new MovieEventException("Event type must be CREATED");
                }
            })
            .then()
            .log("Save actor and movie ");
    }

    @Override
    public Mono<Void> updateActorMovie(ActorMovieDTO actorMovieDTO) {
        return Mono.just(movieEventConsumer.consumerForActor())
            .flatMapMany(movieEvent -> {
                if (movieEvent.getEventType() == EventType.UPDATED) {
                    Mono<Void> deleteExisting = actorMovieRepository.deleteByMovieId(movieEvent.getMovieId());

                    Flux<ActorMoviePK> newActorMovie = Flux.fromIterable(actorMovieDTO.getActorId())
                            .map(actorId -> new ActorMoviePK(actorId,movieEvent.getMovieId()));

                    return deleteExisting.thenMany(newActorMovie)
                            .collectList()
                            .flatMapMany(actorMovieRepository::saveAll);
                }else {
                    throw new MovieEventException("Event type must be UPDATED");
                }
            })
            .then();
    }

    @Override
    public Flux<Actor> findActorByMovieId(Long movieId) {
        return actorMovieRepository.findActorByMovieId(movieId);
    }

    @Override
    public Flux<Movie> findMovieByActor(Long id) {
        return actorMovieRepository.findMovieByActorId(id)
                .log("Find movie by actor id: " + id);
    }
}
