package com.app.Service.Impl;

import com.app.DTO.GenreDTO;
import com.app.Entity.Genre;
import com.app.Entity.GenreMoviePK;
import com.app.Entity.Movie;
import com.app.Expection.GenreNotFound;
import com.app.Repository.GenreMovieRepository;
import com.app.Repository.GenreRepository;
import com.app.Service.GenreService;
import com.app.messaging.processor.Processor;
import com.app.messaging.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreMovieRepository genreMovieRepository;
    private final Producer<Genre> genreProducer;
    private final Processor<Movie> movieProcessor;
    private final ReactiveRedisTemplate<String,Genre> redisTemplate;

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository, GenreMovieRepository genreMovieRepository, Producer<Genre> genreProducer, Processor<Movie> movieProcessor, ReactiveRedisTemplate<String, Genre> redisTemplate) {
        this.genreRepository = genreRepository;
        this.genreMovieRepository = genreMovieRepository;
        this.genreProducer = genreProducer;
        this.movieProcessor = movieProcessor;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Flux<Genre> findAll() {
        return redisTemplate.keys("genre:*")
            .flatMap(key -> redisTemplate.opsForValue().get(key))
            .thenMany(genreRepository.findAll()
                .flatMap(genre ->
                    redisTemplate
                    .opsForValue()
                    .set("genre:" + genre.getId(),genre, Duration.ofHours(24))
                    .thenReturn(genre)))
            .log("Find all genres");
    }

    @Override
    public Mono<Genre> findById(Long id) {
        return redisTemplate.opsForValue().get("genre:"+ id)
            .switchIfEmpty(genreRepository.findById(id)
                .switchIfEmpty(Mono.error(new GenreNotFound("Genre not found with a id: " + id)))
                .flatMap(genre ->
                    redisTemplate
                    .opsForValue()
                    .set("genre:" + genre.getId(),genre,Duration.ofHours(24))
                    .thenReturn(genre)))
            .log("Find a genre with a id: " + id);
    }

    @Override
    public Flux<Genre> findGenreByMovieId(Long movieId) {
        return genreMovieRepository.findByMovieId(movieId)
            .doOnNext(genreProducer::send)
            .log("Find a genre with a movie id: " + movieId);
    }

    @Override
    public Mono<GenreMoviePK> save(GenreDTO genreDTO) {
        return movieProcessor.getOneData()
            .flatMap(movie ->
                genreMovieRepository.save(new GenreMoviePK(genreDTO.getGenreId(),movie.getId()))
                    .onErrorResume(error -> Mono.error(new RuntimeException("Failed to save genre movie", error)))
            );
    }
}
