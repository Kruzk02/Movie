package com.app.module.movie.service.impl;

import com.app.module.movie.dto.MovieDTO;
import com.app.Entity.*;
import com.app.Expection.MovieNotFound;
import com.app.module.movie.mapper.MovieMapper;
import com.app.module.movie.service.MovieService;
import com.app.messaging.producer.MovieEventProducer;
import com.app.messaging.producer.UserActivityProducer;
import lombok.AllArgsConstructor;
import com.app.module.movie.entity.Movie;
import com.app.module.movie.repository.MovieRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

import static com.app.constants.AppConstants.MOVIE_POSTER;

@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {

    private static final Logger log = LogManager.getLogger(MovieServiceImpl.class);
    private final MovieRepository movieRepository;
    private final MovieEventProducer movieEventProducer;
    private final UserActivityProducer activityProducer;
    private final ReactiveRedisTemplate<String, Movie> redisTemplate;


    /**
     * Find all movies, fetching from cache if available, otherwise from the database
     *
     * @return a Flux of movies.
     */
    @Override
    public Flux<Movie> findAll() {
        return redisTemplate.keys("movie:*")
            .flatMap(key -> redisTemplate.opsForValue().get(key))
            .thenMany(
                movieRepository.findAll().flatMap(movie -> redisTemplate
                    .opsForValue()
                    .set("movie:" + movie.getId(), movie, Duration.ofHours(24))
                    .thenReturn(movie)
                )
            )
        .log("Find all movie");
    }

    @Override
    public Mono<Movie> findByIdAndReceiveUserId(Long id, Long userId) {
        return redisTemplate.opsForValue().get("movie:" + id)
            .switchIfEmpty(
                movieRepository.findById(id)
                    .switchIfEmpty(Mono.error(new MovieNotFound("Movie not found with a id: " + id)))
                    .flatMap(movie ->
                        redisTemplate
                            .opsForValue()
                            .set("movie:" + movie.getId(), movie, Duration.ofHours(24))
                            .thenReturn(movie)
                    )
            )
            .doOnNext(movie -> activityProducer.send(new UserActivity(userId,movie)))
            .doOnError(e -> log.error("Error fetching a movie with id: {} ", id, e))
            .log("Find movie with id: " + id);
    }

    /**
     * Finds a movie by its ID, fetching from cache if available, otherwise from the database.
     *
     * @param id the ID of the movie to find.
     *
     * @return a Mono of the found movie, or an error find not found.
     */
    @Override
    public Mono<Movie> findById(Long id) {
        return redisTemplate.opsForValue().get("movie:" + id)
            .switchIfEmpty(
                movieRepository.findById(id)
                    .switchIfEmpty(Mono.error(new MovieNotFound("Movie not found with a id: " + id)))
                    .flatMap(movie ->
                        redisTemplate
                            .opsForValue()
                            .set("movie:" + movie.getId(), movie, Duration.ofHours(24))
                            .thenReturn(movie)
                    )
            )
        .doOnError(e -> log.error("Error fetching a movie with id: {} ", id, e))
        .log("Find movie with id: " + id);
    }

    /**
     * Saves a new movie.
     *
     * @param movieDTO the movie data transfer from object containing movie details.
     * @return a Mono of the saved movie.
     */
    @Override
    public Mono<Movie> save(MovieDTO movieDTO) {
        Movie movie = MovieMapper.INSTANCE.mapDtoToEntity(movieDTO);
        return movieRepository.save(movie)
                .flatMap(savedMovie -> redisTemplate
                        .opsForValue()
                        .set("movie:"+savedMovie.getId(),movie,Duration.ofHours(24))
                        .thenReturn(savedMovie)
                )
                .doOnSuccess(savedMovie -> movieEventProducer.send(new MovieEvent(savedMovie.getId(),EventType.CREATED,Instant.now())))
                .log("Save a new movie: " + movie);
    }

    /**
     * Updates an existing movie by its ID.
     * @param id the ID of the movie to update.
     * @param movieDTO the movie data transfer object containing updated movie details.
     * @return a Mono of the updated movie.
     */
    @Override
    public Mono<Movie> update(Long id, MovieDTO movieDTO) {
        return movieRepository.findById(id)
            .switchIfEmpty(Mono.error(new MovieNotFound("Movie not found with id: " + id)))
            .flatMap(existingMovie -> {

                    Path path = Paths.get(MOVIE_POSTER + existingMovie.getPoster());
                    File file = path.toFile();
                    if (file.exists() && file.isFile()) {
                        file.delete();
                    }

                    existingMovie.setTitle(movieDTO.getTitle());
                    existingMovie.setReleaseYear(movieDTO.getReleaseYear());
                    existingMovie.setDescription(movieDTO.getDescription());
                    existingMovie.setSeasons(movieDTO.getSeasons());
                    existingMovie.setPoster(movieDTO.getPoster());

                return movieRepository.save(existingMovie)
                    .flatMap(updatedMovie -> redisTemplate
                            .opsForValue()
                            .set("movie:" + updatedMovie.getId(), updatedMovie, Duration.ofHours(24))
                            .thenReturn(updatedMovie)
                    )
                    .doOnSuccess(updatedMovie -> movieEventProducer.send(new MovieEvent(updatedMovie.getId(),EventType.UPDATED,Instant.now())));
                }
            ).log("Update a Movie with id: " + id);
    }

    /**
     * Deletes a movie by its ID.
     * @param id the ID of the movie to delete.
     * @return A Mono signalling completion.
     */
    @Override
    public Mono<Void> delete(Long id) {
        return movieRepository.findById(id)
            .switchIfEmpty(Mono.error(new MovieNotFound("Movie not found with a id: " + id)))
            .flatMap(movie -> {
                Path path = Paths.get(MOVIE_POSTER + movie.getPoster());
                File file = path.toFile();
                if (file.exists() && file.isFile()) {
                    file.delete();
                }

                return movieRepository.delete(movie)
                        .then(redisTemplate.opsForValue().delete("movie:" + movie.getId()));
            })
            .log("Delete a movie with a id: " + id).then();
    }
}
