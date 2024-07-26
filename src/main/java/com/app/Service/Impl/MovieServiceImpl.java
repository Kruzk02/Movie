package com.app.Service.Impl;

import com.app.DTO.MovieDTO;
import com.app.Entity.*;
import com.app.Expection.MovieNotFound;
import com.app.Mapper.MovieMapper;
import com.app.Repository.*;
import com.app.Service.FileService;
import com.app.Service.MovieService;
import com.app.messaging.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final FileService fileService;
    private final MessagingService<Movie> movieMessagingService;
    private final ReactiveRedisTemplate<String,Movie> redisTemplate;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService, MessagingService<Movie> movieMessagingService, ReactiveRedisTemplate<String, Movie> redisTemplate) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
        this.movieMessagingService = movieMessagingService;
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
    public Mono<Movie> save(MovieDTO movieDTO, FilePart filePart, String filename) {
        return fileService.save(filePart,"moviePoster",filename)
            .then(Mono.fromCallable(() ->
                {
                    Movie movie = MovieMapper.INSTANCE.mapDtoToEntity(movieDTO);
                    movie.setPoster(filename);
                    return movie;
                })
                .flatMap(movie ->
                    movieRepository.save(movie)
                    .doOnNext(movieMessagingService::sendMessage)
                    .log("Save a new movie: " + movie))
            );
    }

    @Override
    public Mono<Movie> update(Long id, MovieDTO movieDTO, FilePart filePart,String filename) {
        return movieRepository.findById(id)
            .switchIfEmpty(Mono.error(new MovieNotFound("Movie not found with id: " + id)))
            .flatMap(existingMovie ->
                fileService.delete("moviePoster",existingMovie.getPoster())
                    .then(fileService.save(filePart,"moviePoster",filename)
                        .then(Mono.fromCallable(() -> {
                            existingMovie.setTitle(movieDTO.getTitle());
                            existingMovie.setReleaseYear(movieDTO.getRelease_year());
                            existingMovie.setDescription(movieDTO.getDescription());
                            existingMovie.setSeasons(movieDTO.getSeasons());
                            existingMovie.setPoster(filename);

                            movieMessagingService.sendMessage(existingMovie);
                            return existingMovie;
                        }))
                        .flatMap(movieRepository::save)
                    )
            ).log("Update a Movie with id: " + id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return movieRepository.findById(id)
            .switchIfEmpty(Mono.error(new MovieNotFound("Movie not found with a id: " + id)))
            .flatMap(movie -> {
                fileService.delete("moviePoster",movie.getPoster());
                return movieRepository.delete(movie);
            })
            .log("Delete a movie with a id: " + id);
    }
}
