package com.app.module.genre.service.impl;

import com.app.module.genre.dto.GenreDTO;
import com.app.messaging.event.EventType;
import com.app.module.genre.entity.Genre;
import com.app.module.genre.entity.GenreMoviePK;
import com.app.module.movie.entity.Movie;
import com.app.Expection.MovieEventException;
import com.app.module.genre.repository.GenreMovieRepository;
import com.app.module.genre.service.GenreMovieService;
import com.app.messaging.consumer.MovieEventConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GenreMovieServiceImpl implements GenreMovieService {

    private final GenreMovieRepository genreMovieRepository;
    private final MovieEventConsumer movieEventConsumer;

    @Autowired
    public GenreMovieServiceImpl(GenreMovieRepository genreMovieRepository, MovieEventConsumer movieEventConsumer) {
        this.genreMovieRepository = genreMovieRepository;
        this.movieEventConsumer = movieEventConsumer;
    }

    /**
     * Retrieves genres by movie ID.
     *
     * @param movieId The ID of the movie.
     * @return A Flux emitting genres associated with the movie ID.
     */
    @Override
    public Flux<Genre> findGenreByMovieId(Long movieId) {
        return genreMovieRepository.findByMovieId(movieId)
                .log("Find a genre with a movie id: " + movieId);
    }

    /**
     * Saves a new genre movie.
     *
     * @param genreDTO The GenreDTO containing the details of the genre movie to be saved.
     * @return A Mono emitting the saved genre movie.
     */
    @Override
    public Mono<Void> saveGenreMovie(GenreDTO genreDTO) {
        return Mono.just(movieEventConsumer.consumerForGenre())
            .filter(movieEvent -> movieEvent.getEventType() == EventType.CREATED)
            .flatMapMany(movieEvent -> {
                if (movieEvent.getEventType() == EventType.CREATED) {
                    Set<GenreMoviePK> genreMovies = genreDTO.getGenreId().stream()
                            .map(genreId -> new GenreMoviePK(genreId, movieEvent.getMovieId()))
                            .collect(Collectors.toSet());
                    return genreMovieRepository.saveAll(genreMovies);
                }else {
                    throw new MovieEventException("Event type must be CREATED");
                }
            })
            .then()
            .log("Save genre and movie");
    }

    @Override
    public Mono<Void> updateGenreMovie(GenreDTO genreDTO) {
        return Mono.just(movieEventConsumer.consumerForGenre())
            .flatMapMany(movieEvent -> {
                if (movieEvent.getEventType() == EventType.UPDATED) {
                    Mono<Void> deleteExisting = genreMovieRepository.deleteByMovieId(movieEvent.getMovieId());

                    Flux<GenreMoviePK> newGenreMovie = Flux.fromIterable(genreDTO.getGenreId())
                            .map(genreId -> new GenreMoviePK(genreId, movieEvent.getMovieId()));

                    return deleteExisting.thenMany(newGenreMovie)
                            .collectList()
                            .flatMapMany(genreMovieRepository::saveAll);
                }else {
                    throw new MovieEventException("Event type must be UPDATED");
                }
            })
            .then();
    }

    /**
     * Retrieves movies by genre ID.
     *
     * @param id The ID of the genre.
     * @return A Flux emitting movies associated with the genre ID.
     */
    @Override
    public Flux<Movie> findMovieByGenreId(Long id) {
        return genreMovieRepository.findMovieByGenreId(id)
                .log("Find a movie with a id: " + id);
    }
}
