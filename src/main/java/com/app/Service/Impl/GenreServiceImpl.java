package com.app.Service.Impl;

import com.app.Entity.Genre;
import com.app.Expection.GenreNotFound;
import com.app.Repository.GenreRepository;
import com.app.Service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final ReactiveRedisTemplate<String,Genre> redisTemplate;

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository, ReactiveRedisTemplate<String, Genre> redisTemplate) {
        this.genreRepository = genreRepository;
        this.redisTemplate = redisTemplate;
    }

    /**
     * Retrieves all genres
     * @return A Flux emitting all genres
     */
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

    /**
     * Retrieves a genre by its id.
     * @param id The ID of the genres.
     * @return A Mono emitting the genre if found. otherwise an empty Mono.
     */
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
}
