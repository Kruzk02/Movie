package com.app.Service.Impl;

import com.app.DTO.MovieDTO;
import com.app.Entity.*;
import com.app.Expection.MovieNotFound;
import com.app.Mapper.MovieMapper;
import com.app.Repository.*;
import com.app.Service.MovieService;
import com.app.messaging.producer.MovieProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class MovieServiceImpl implements MovieService {
    
    private final MovieRepository movieRepository;
    private final MovieProducer movieProducer;
    private final ReactiveRedisTemplate<String,Movie> redisTemplate;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, MovieProducer movieProducer, ReactiveRedisTemplate<String, Movie> redisTemplate) {
        this.movieRepository = movieRepository;
        this.movieProducer = movieProducer;
        this.redisTemplate = redisTemplate;
    }

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
        .log("Find movie with id: " + id);
    }

    @Override
    public Mono<Movie> save(MovieDTO movieDTO) {
        Movie movie = MovieMapper.INSTANCE.mapDtoToEntity(movieDTO);
        return movieRepository.save(movie).log("Save a new movie: " + movie);
    }

    @Override
    public Mono<Movie> update(Long id, MovieDTO movieDTO) {
        return movieRepository.findById(id)
            .switchIfEmpty(Mono.error(new MovieNotFound("Movie not found with id: " + id)))
            .flatMap(existingMovie -> {
                existingMovie.setTitle(movieDTO.getTitle());
                existingMovie.setReleaseYear(movieDTO.getRelease_year());
                existingMovie.setMovieLength(movieDTO.getMovie_length());

                return movieRepository.save(existingMovie);
            })
        .log("Update a Movie with id: " + id);
    }


    @Override
    public Mono<Void> delete(Long id) {
        return movieRepository.findById(id)
            .switchIfEmpty(Mono.error(new MovieNotFound("Movie not found with a id: " + id)))
            .flatMap(movieRepository::delete)
            .log("Delete a movie with a id: " + id);
    }
}
