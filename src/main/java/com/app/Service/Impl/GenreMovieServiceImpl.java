package com.app.Service.Impl;

import com.app.DTO.GenreDTO;
import com.app.Entity.Genre;
import com.app.Entity.GenreMoviePK;
import com.app.Entity.Movie;
import com.app.Repository.GenreMovieRepository;
import com.app.Service.GenreMovieService;
import com.app.messaging.processor.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GenreMovieServiceImpl implements GenreMovieService {

    private final GenreMovieRepository genreMovieRepository;
    private final Processor<Movie> movieProcessor;

    @Autowired
    public GenreMovieServiceImpl(GenreMovieRepository genreMovieRepository, Processor<Movie> movieProcessor) {
        this.genreMovieRepository = genreMovieRepository;
        this.movieProcessor = movieProcessor;
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
    public Mono<GenreMoviePK> save(GenreDTO genreDTO) {
        return movieProcessor.getOneData()
                .flatMap(movie ->
                        genreMovieRepository.save(new GenreMoviePK(genreDTO.getGenreId(),movie.getId()))
                                .onErrorResume(error -> Mono.error(new RuntimeException("Failed to save genre movie", error)))
                );
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
