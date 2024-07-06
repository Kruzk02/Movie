package com.app.Service.Impl;

import com.app.DTO.ActorMovieDTO;
import com.app.Entity.Actor;
import com.app.Entity.ActorMoviePK;
import com.app.Entity.Movie;
import com.app.Repository.ActorMovieRepository;
import com.app.Service.ActorMovieService;
import com.app.messaging.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ActorMovieServiceImpl implements ActorMovieService {

    private final ActorMovieRepository actorMovieRepository;
    private final MessagingService<Movie> movieMessagingService;

    @Autowired
    public ActorMovieServiceImpl(ActorMovieRepository actorMovieRepository, MessagingService<Movie> movieMessagingService) {
        this.actorMovieRepository = actorMovieRepository;
        this.movieMessagingService = movieMessagingService;
    }

    @Override
    public Mono<Void> saveActorMovie(ActorMovieDTO actorMovieDTO) {
        return movieMessagingService.receiveMessage()
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
    public Mono<Void> updateActorMovie(ActorMovieDTO actorMovieDTO) {
        return movieMessagingService.receiveMessage()
            .flatMapMany(movie -> {
                Mono<Void> deleteExisting = actorMovieRepository.deleteByMovieId(movie.getId());

                Flux<ActorMoviePK> newActorMovie = Flux.fromIterable(actorMovieDTO.getActorId())
                        .map(actorId -> new ActorMoviePK(actorId,movie.getId()));

                return deleteExisting.thenMany(newActorMovie)
                        .collectList()
                        .flatMapMany(actorMovieRepository::saveAll);
            }).then();
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
