package com.app.module.movie.service.impl;

import com.app.module.movie.dto.MovieMediaDTO;
import com.app.module.movie.entity.MovieMedia;
import com.app.Expection.MovieMediaNotFound;
import com.app.module.movie.mapper.MovieMediaMapper;
import com.app.module.movie.repository.MovieMediaRepository;
import com.app.module.movie.service.MovieMediaService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

@Service
public class MovieMediaServiceImpl implements MovieMediaService {

    private final MovieMediaRepository movieMediaRepository;
    private final ReactiveRedisTemplate<String,MovieMedia> redisTemplate;

    @Autowired
    public MovieMediaServiceImpl(MovieMediaRepository movieMediaRepository, ReactiveRedisTemplate<String, MovieMedia> redisTemplate) {
        this.movieMediaRepository = movieMediaRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Flux<MovieMedia> findAllByMovieId(Long movieId) {
        return redisTemplate.keys("movie_media_movieId:" + movieId)
            .flatMap(key -> redisTemplate.opsForValue().get(key))
            .switchIfEmpty(
                movieMediaRepository.findAllByMovieId(movieId)
                    .switchIfEmpty(Mono.error(new MovieMediaNotFound("Movie media not found with a movie id: " + movieId)))
                    .flatMap(movieMedia ->
                        redisTemplate
                            .opsForValue()
                            .set("movie_media_movieId:" + movieMedia.getMovieId(), movieMedia, Duration.ofHours(12))
                            .thenReturn(movieMedia)
                        )
            );
    }

    @Override
    public Flux<MovieMedia> findAllByMovieIdAndQuality(Long movieId,String quality) {
        return redisTemplate.keys("movie_media:" + movieId + ":quality:" + quality)
            .flatMap(redisTemplate.opsForValue()::get)
            .switchIfEmpty(
                movieMediaRepository.findAllByMovieIdAndQuality(movieId,quality)
                    .switchIfEmpty(Mono.error(new MovieMediaNotFound("Movie media not found with a movie id: " + movieId + "or quality: " + quality)))
                    .flatMap(movieMedia ->
                        redisTemplate
                            .opsForValue()
                            .set("movie_media:" + movieId + ":quality:" + movieMedia.getQuality(),movieMedia,Duration.ofHours(12))
                            .thenReturn(movieMedia)
                    )
            );
    }

    @Override
    public Mono<MovieMedia> findByMovieIdAndEpisode(Long movieId, byte episode) {
        return redisTemplate.opsForValue().get("movie_media:" + movieId + ":episode:" + episode)
            .switchIfEmpty(
                movieMediaRepository.findByMovieIdAndEpisode(movieId,episode)
                    .switchIfEmpty(Mono.error(new MovieMediaNotFound("Movie media not found with a movie id:" + movieId + "or episode: " + episode)))
                    .flatMap(movieMedia ->
                        redisTemplate
                            .opsForValue()
                            .set("movie_media:" + movieId + ":episode:" + episode,movieMedia,Duration.ofHours(12))
                            .thenReturn(movieMedia)
                    )
            );
    }

    @Override
    public Mono<MovieMedia> save(MovieMediaDTO movieMediaDTO) {
        MovieMedia movieMedia = MovieMediaMapper.INSTANCE.mapDtoToEntity(movieMediaDTO);

        var filePart = movieMediaDTO.video();
        int lastIndexOfDot = filePart.filename().lastIndexOf('.');
        String extension = "";
        if (lastIndexOfDot != 1) {
            extension = filePart.filename().substring(lastIndexOfDot);
        }

        String filename = RandomStringUtils.randomAlphabetic(20);
        filename += extension.replaceAll("[(){}]","");

        var path = Paths.get("movieMedia/" + filename);
        movieMedia.setFilePath(filename);

        return filePart.transferTo(path).then(
            movieMediaRepository.save(movieMedia)
                .flatMap(savedMovieMedia -> redisTemplate
                    .opsForValue()
                    .set("movie_media:" + savedMovieMedia.getId(),savedMovieMedia,Duration.ofHours(2))
                    .thenReturn(savedMovieMedia))
                .log("Save a new movie media")
        );
    }

    @Override
    public Mono<MovieMedia> update(Long id, MovieMediaDTO movieMediaDTO) {
        return movieMediaRepository.findById(id)
            .switchIfEmpty(Mono.error(new MovieMediaNotFound("Movie media not found")))
            .flatMap(existingMovieMedia -> {

                Path path = Paths.get("movieMedia/" + existingMovieMedia.getFilePath());
                File file = path.toFile();

                if (file.exists() && file.isFile()) {
                    var isDeleted = file.delete();
                }

                var filePart = movieMediaDTO.video();
                int lastIndexOfDot = filePart.filename().lastIndexOf('.');
                String extension = "";
                if (lastIndexOfDot != 1) {
                    extension = filePart.filename().substring(lastIndexOfDot);
                }

                String filename = RandomStringUtils.randomAlphabetic(20);
                filename += extension.replaceAll("[(){}]","");

                Path newPath = Paths.get("movieMedia/" + filename);

                existingMovieMedia.setFilePath(filename);
                existingMovieMedia.setMovieId(movieMediaDTO.movieId());
                existingMovieMedia.setDuration(movieMediaDTO.duration());
                existingMovieMedia.setEpisodes(movieMediaDTO.episodes());
                existingMovieMedia.setQuality(movieMediaDTO.quality());

                return filePart.transferTo(newPath).then(
                    movieMediaRepository.save(existingMovieMedia)
                        .flatMap(updatedMovieMedia -> redisTemplate
                            .opsForValue()
                            .set("movie_media:" + updatedMovieMedia.getId(),updatedMovieMedia,Duration.ofHours(24))
                            .thenReturn(updatedMovieMedia))
                );
            })
            .log("Update movie media with a id: " + id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return movieMediaRepository.findById(id)
            .flatMap(movieMedia -> {
                Path path = Paths.get("movieMedia/" + movieMedia.getFilePath());
                File file = path.toFile();
                if (file.exists() && file.isFile()) {
                    file.delete();
                }
                return movieMediaRepository.delete(movieMedia)
                        .then(redisTemplate
                                .opsForValue()
                                .delete("movie_media:" + movieMedia.getId()));
            })
            .log("Delete movie media with a id: " + id).then();
    }
}
