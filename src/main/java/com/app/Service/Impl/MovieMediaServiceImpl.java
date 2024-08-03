package com.app.Service.Impl;

import com.app.DTO.MovieMediaDTO;
import com.app.Entity.MovieMedia;
import com.app.Expection.MovieMediaNotFound;
import com.app.Mapper.MovieMediaMapper;
import com.app.Repository.MovieMediaRepository;
import com.app.Service.FileService;
import com.app.Service.MovieMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class MovieMediaServiceImpl implements MovieMediaService {

    private final MovieMediaRepository movieMediaRepository;
    private final ReactiveRedisTemplate<String,MovieMedia> redisTemplate;
    private final FileService fileService;

    @Autowired
    public MovieMediaServiceImpl(MovieMediaRepository movieMediaRepository, ReactiveRedisTemplate<String, MovieMedia> redisTemplate, FileService fileService) {
        this.movieMediaRepository = movieMediaRepository;
        this.redisTemplate = redisTemplate;
        this.fileService = fileService;
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
    public Mono<MovieMedia> save(MovieMediaDTO movieMediaDTO, FilePart filePart, String filename) {
        return fileService.save(filePart,"movieMedia",filename)
                .then(Mono.fromCallable(() -> {
                    MovieMedia movieMedia = MovieMediaMapper.INSTANCE.mapDtoToEntity(movieMediaDTO);
                    movieMedia.setFilePath(filename);
                    return movieMedia;
                })
                .flatMap(movieMediaRepository::save)).log("Save a movie media");
    }

    @Override
    public Mono<MovieMedia> update(Long id, MovieMediaDTO movieMediaDTO, FilePart filePart, String filename) {
        return movieMediaRepository.findById(id)
            .flatMap(existingMovieMedia ->
                fileService.delete("movieMedia",existingMovieMedia.getFilePath())
                    .then(fileService.save(filePart,"movieMedia",filename)
                        .then(Mono.fromCallable(() -> {
                            existingMovieMedia.setFilePath(filename);
                            existingMovieMedia.setMovieId(movieMediaDTO.getMovieId());
                            existingMovieMedia.setDuration(movieMediaDTO.getDuration());
                            existingMovieMedia.setEpisodes(movieMediaDTO.getEpisodes());
                            existingMovieMedia.setQuality(movieMediaDTO.getQuality());

                            return existingMovieMedia;
                        }))
                        .flatMap(movieMediaRepository::save)
                    )
            );
    }

    @Override
    public Mono<Void> delete(Long id) {
        return movieMediaRepository.findById(id)
            .flatMap(movieMedia -> {
                fileService.delete("movieMedia",movieMedia.getFilePath());
                return movieMediaRepository.delete(movieMedia);
            })
            .log("Delete movie media with a id: " + id);
    }
}
