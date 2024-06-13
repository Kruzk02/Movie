package com.app.Service.Impl;

import com.app.DTO.ActorMovieDTO;
import com.app.Entity.Actor;
import com.app.Entity.ActorMoviePK;
import com.app.Entity.Movie;
import com.app.Repository.ActorMovieRepository;
import com.app.Service.ActorMovieService;
import com.app.messaging.processor.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ActorMovieServiceImpl implements ActorMovieService {

    private final ActorMovieRepository actorMovieRepository;
    private final Processor<Movie> movieProcessor;

    @Autowired
    public ActorMovieServiceImpl(ActorMovieRepository actorMovieRepository, Processor<Movie> movieProcessor) {
        this.actorMovieRepository = actorMovieRepository;
        this.movieProcessor = movieProcessor;
    }

    @Override
    public Mono<Void> saveActorMovie(ActorMovieDTO actorMovieDTO) {
        return movieProcessor.getOneData()
            .flatMapMany(movie -> {
                Set<ActorMoviePK> actorMovies = actorMovieDTO.getActorId().stream()
                    .map(actorId -> new ActorMoviePK(actorId,movie.getId()))
                    .collect(Collectors.toSet());
                return actorMovieRepository.saveAll(actorMovies);
            })
            .then()
            .log("Save actor and movie ");
    }

    @Override
    public Mono<ActorMoviePK> updateActorMovie(ActorMovieDTO actorMovieDTO) {
        return null;
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
