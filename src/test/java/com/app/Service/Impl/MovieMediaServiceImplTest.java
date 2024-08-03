package com.app.Service.Impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;

import com.app.DTO.MovieMediaDTO;
import com.app.Entity.MovieMedia;
import com.app.Repository.MovieMediaRepository;
import com.app.Service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.http.codec.multipart.FilePart;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class MovieMediaServiceImplTest {

    @Mock
    private MovieMediaRepository movieMediaRepository;

    @Mock
    private ReactiveRedisTemplate<String, MovieMedia> redisTemplate;

    @Mock
    private ReactiveValueOperations<String, MovieMedia> reactiveValueOperations;

    @Mock
    private FileService fileService;

    @InjectMocks
    private MovieMediaServiceImpl movieMediaServiceImpl;

    private MovieMedia movieMedia;

    @BeforeEach
    public void setUp() {
        movieMedia = new MovieMedia();
        movieMedia.setId(1L);
        movieMedia.setMovieId(1L);
        movieMedia.setDuration(LocalTime.from(Instant.MAX));
        movieMedia.setFilePath("no.mp4");
        movieMedia.setQuality("720P");
        movieMedia.setEpisodes((byte) 1);

    }

    @Test
    public void testFindAllByMovieId() {
        Long movieId = 1L;

        when(redisTemplate.opsForValue()).thenReturn(reactiveValueOperations);
        when(redisTemplate.keys(any())).thenReturn(Flux.empty());
        when(movieMediaRepository.findAllByMovieId(movieId)).thenReturn(Flux.just(movieMedia));
        when(reactiveValueOperations.set(any(), any(), any(Duration.class))).thenReturn(Mono.just(true));

        Flux<MovieMedia> result = movieMediaServiceImpl.findAllByMovieId(movieId);

        StepVerifier.create(result)
                .expectNext(movieMedia)
                .verifyComplete();
    }

    @Test
    public void testFindAllByMovieIdAndQuality() {
        Long movieId = 1L;
        String quality = "720P";

        when(redisTemplate.opsForValue()).thenReturn(reactiveValueOperations);
        when(redisTemplate.keys(any())).thenReturn(Flux.empty());
        when(movieMediaRepository.findAllByMovieIdAndQuality(movieId,quality)).thenReturn(Flux.just(movieMedia));
        when(reactiveValueOperations.set(any(), any(), any(Duration.class))).thenReturn(Mono.just(true));

        StepVerifier.create(movieMediaServiceImpl.findAllByMovieIdAndQuality(movieId, quality))
                .expectNext(movieMedia)
                .verifyComplete();
    }

    @Test
    public void testFindByMovieIdAndEpisode() {
        Long movieId = 1L;
        byte episode = 1;

        when(redisTemplate.opsForValue()).thenReturn(reactiveValueOperations);
        when(redisTemplate.keys(any())).thenReturn(Flux.empty());
        when(movieMediaRepository.findByMovieIdAndEpisode(movieId,episode)).thenReturn(Mono.just(movieMedia));
        when(reactiveValueOperations.set(any(), any(), any(Duration.class))).thenReturn(Mono.just(true));

        StepVerifier.create(movieMediaServiceImpl.findByMovieIdAndEpisode(movieId, episode))
                .expectNext(movieMedia)
                .verifyComplete();
    }

    @Test
    public void testSave() {
        MovieMediaDTO movieMediaDTO = new MovieMediaDTO();
        FilePart filePart = mock(FilePart.class);
        String filename = "test.mp4";
        MovieMedia movieMedia = new MovieMedia();

        when(fileService.save(filePart, "movieMedia", filename)).thenReturn(Mono.empty());
        when(movieMediaRepository.save(any(MovieMedia.class))).thenReturn(Mono.just(movieMedia));

        StepVerifier.create(movieMediaServiceImpl.save(movieMediaDTO, filePart, filename))
                .expectNext(movieMedia)
                .verifyComplete();
    }

    @Test
    public void testUpdate() {
        Long id = 1L;
        MovieMediaDTO movieMediaDTO = new MovieMediaDTO();
        FilePart filePart = mock(FilePart.class);
        String filename = "updated.mp4";
        MovieMedia existingMovieMedia = new MovieMedia();

        when(movieMediaRepository.findById(id)).thenReturn(Mono.just(existingMovieMedia));
        when(fileService.delete("movieMedia", existingMovieMedia.getFilePath())).thenReturn(Mono.empty());
        when(fileService.save(filePart, "movieMedia", filename)).thenReturn(Mono.empty());
        when(movieMediaRepository.save(any(MovieMedia.class))).thenReturn(Mono.just(existingMovieMedia));

        StepVerifier.create(movieMediaServiceImpl.update(id, movieMediaDTO, filePart, filename))
                .expectNext(existingMovieMedia)
                .verifyComplete();
    }

    @Test
    public void updateWithMovieId() {
        Long movieId = 1L;
        MovieMediaDTO movieMediaDTO = new MovieMediaDTO();
        FilePart filePart = mock(FilePart.class);
        String filename = "updated.mp4";
        MovieMedia existingMovieMedia = new MovieMedia();

        when(movieMediaRepository.findAllByMovieId(movieId)).thenReturn(Flux.just(existingMovieMedia));
        when(fileService.delete("movieMedia", existingMovieMedia.getFilePath())).thenReturn(Mono.empty());
        when(fileService.save(filePart, "movieMedia", filename)).thenReturn(Mono.empty());
        when(movieMediaRepository.updateWithMovieId(any(),any(),any(),any(),any())).thenReturn(Mono.just(any()));

        StepVerifier.create(movieMediaServiceImpl.updateWithMovieId(movieId, movieMediaDTO, filePart, filename))
                .expectNext()
                .verifyComplete();
    }

    @Test
    public void testDelete() {
        Long id = 1L;
        MovieMedia movieMedia = new MovieMedia();

        when(movieMediaRepository.findById(id)).thenReturn(Mono.just(movieMedia));
        when(movieMediaRepository.delete(movieMedia)).thenReturn(Mono.empty());

        StepVerifier.create(movieMediaServiceImpl.delete(id))
                .verifyComplete();
    }
}
