package com.app.Service.Impl;

import com.app.DTO.DirectorMovieDTO;
import com.app.Entity.Director;
import com.app.Entity.DirectorMoviePK;
import com.app.Entity.EventType;
import com.app.Entity.Movie;
import com.app.Expection.MovieEventException;
import com.app.Repository.DirectorMovieRepository;
import com.app.Service.DirectorMovieService;
import com.app.messaging.consumer.MovieEventConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DirectorMovieServiceImpl implements DirectorMovieService {

    private final DirectorMovieRepository directorMovieRepository;
    private final MovieEventConsumer movieEventConsumer;

    @Autowired
    public DirectorMovieServiceImpl(DirectorMovieRepository directorMovieRepository, MovieEventConsumer movieEventConsumer) {
        this.directorMovieRepository = directorMovieRepository;
        this.movieEventConsumer = movieEventConsumer;
    }

    @Override
    public Mono<Void> saveDirectorMovie(DirectorMovieDTO directorMovieDTO) {
        return Mono.just(movieEventConsumer.consumerForDirector())
            .flatMapMany(movieEvent -> {
                if (movieEvent.getEventType() == EventType.CREATED) {
                    Set<DirectorMoviePK> directorMovies = directorMovieDTO.getDirectorId().stream()
                            .map(directorId -> new DirectorMoviePK(directorId,movieEvent.getMovieId()))
                            .collect(Collectors.toSet());
                    return directorMovieRepository.saveAll(directorMovies);
                } else {
                    throw new MovieEventException("Event type must be CREATED");
                }
            })
            .then()
            .log("Save director and movie");
    }

    @Override
    public Mono<Void> updateDirectorMovie(DirectorMovieDTO directorMovieDTO) {
        return Mono.just(movieEventConsumer.consumerForDirector())
            .flatMapMany(movieEvent -> {
                if (movieEvent.getEventType() == EventType.UPDATED) {
                    Mono<Void> deleteExisting = directorMovieRepository.deleteByMovieId(movieEvent.getMovieId());

                    Flux<DirectorMoviePK> newDirectorMovie = Flux.fromIterable(directorMovieDTO.getDirectorId())
                            .map(directorId -> new DirectorMoviePK(directorId, movieEvent.getMovieId()));

                    return deleteExisting.thenMany(newDirectorMovie)
                            .collectList()
                            .flatMapMany(directorMovieRepository::saveAll);
                }else {
                    throw new MovieEventException("Event type must be UPDATED");
                }
            })
            .then();
    }

    @Override
    public Flux<Director> findDirectorByMovieId(Long movieId) {
        return directorMovieRepository.findDirectorByMovieId(movieId)
                .log("Find director by movie id: " + movieId);
    }

    @Override
    public Flux<Movie> findMovieByDirector(Long id) {
        return directorMovieRepository.findMovieByDirectorId(id)
                .log("Find movie by director id: " + id);
    }
}
