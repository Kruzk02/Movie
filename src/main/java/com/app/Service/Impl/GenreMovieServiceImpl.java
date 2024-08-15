package com.app.Service.Impl;

import com.app.DTO.GenreDTO;
import com.app.Entity.Genre;
import com.app.Entity.GenreMoviePK;
import com.app.Entity.Movie;
import com.app.Repository.GenreMovieRepository;
import com.app.Service.GenreMovieService;
import com.app.messaging.consumer.GenreConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GenreMovieServiceImpl implements GenreMovieService {

    private final GenreMovieRepository genreMovieRepository;
    private final GenreConsumer genreConsumer;

    @Autowired
    public GenreMovieServiceImpl(GenreMovieRepository genreMovieRepository, GenreConsumer genreConsumer) {
        this.genreMovieRepository = genreMovieRepository;
        this.genreConsumer = genreConsumer;
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
        return Mono.just(genreConsumer.receive())
            .flatMapMany(movie -> {
                Set<GenreMoviePK> genreMovies = genreDTO.getGenreId().stream()
                    .map(genreId -> new GenreMoviePK(genreId,movie.getId()))
                    .collect(Collectors.toSet());
                return genreMovieRepository.saveAll(genreMovies);
            })
            .then()
            .log("Save genre and movie");
    }

    @Override
    public Mono<Void> updateGenreMovie(GenreDTO genreDTO) {
        return Mono.just(genreConsumer.receive())
            .flatMapMany(movie -> {
                Mono<Void> deleteExisting = genreMovieRepository.deleteByMovieId(movie.getId());

                Flux<GenreMoviePK> newGenreMovie = Flux.fromIterable(genreDTO.getGenreId())
                        .map(genreId -> new GenreMoviePK(genreId,movie.getId()));

                return deleteExisting.thenMany(newGenreMovie)
                        .collectList()
                        .flatMapMany(genreMovieRepository::saveAll);
            }).then();
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
