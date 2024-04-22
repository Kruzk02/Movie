package com.app.Service.Impl;

import com.app.DTO.MovieDTO;
import com.app.Entity.Movie;
import com.app.Entity.Rating;
import com.app.Expectation.MovieNotFound;
import com.app.Mapper.MovieMapper;
import com.app.Repository.MovieRepository;
import com.app.Service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *  Service implementation for managing movies.
 */
@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * Retrieves all Movies.
     * @return Flux of all movies.
     */
    @Override
    public Flux<Movie> getAll() {
        return movieRepository.findAll();
    }

    /**
     * Retrieves a movie by its id.
     * @param id The id of the movie to retrieve.
     * @return Mono emitting the movie if found, otherwise emit a MovieNotFound error.
     */
    @Override
    public Mono<Movie> getById(Long id) {
        return movieRepository.findById(id)
                .switchIfEmpty(Mono.error(new MovieNotFound("Movie Not Found")));
    }

    /**
     * Saves a new Movie
     * @param movieDTO the DTO representing the movie to be saved.
     * @return Mono emitting the saved movie.
     */
    @Override
    public Mono<Movie> save(MovieDTO movieDTO) {
        Movie movie = MovieMapper.INSTANCE.movieDtoToMovie(movieDTO);
        return movieRepository.save(movie);
    }

    /**
     * Updates an existing movie.
     * @param id        The id of the movie to update.
     * @param movieDTO  The DTO representing the updated movie data.
     * @return Mono emitting the updated movie if found, Otherwise emits MovieNotFound error.
     */
    @Override
    public Mono<Movie> update(Long id, MovieDTO movieDTO) {
        return movieRepository.findById(id)
                .switchIfEmpty(Mono.error(new MovieNotFound("Movie Not Found")))
                .flatMap(exstingMovie ->{
                    exstingMovie.setTitle(movieDTO.getTitle());
                    exstingMovie.setRating(Enum.valueOf(Rating.class,movieDTO.getRating()));
                    exstingMovie.setDirector(movieDTO.getDirector());
                    exstingMovie.setLength(movieDTO.getLength());
                    exstingMovie.setRelease_year(movieDTO.getRelease_year());
                    exstingMovie.setGenres(movieDTO.getGenres());
                    return movieRepository.save(exstingMovie);
                });
    }

    /**
     * Deletes a movie by its id.
     * @param id The id of the movie to delete.
     * @return Mono emitting void upon successful deletion or emit MovieNotFound error.
     */
    @Override
    public Mono<Void> delete(Long id) {
        return movieRepository.findById(id)
                .switchIfEmpty(Mono.error(new MovieNotFound("Movie Not Found")))
                .flatMap(movieRepository::delete);
    }
}
