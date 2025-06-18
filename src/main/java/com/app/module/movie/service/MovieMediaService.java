package com.app.module.movie.service;

import com.app.module.movie.dto.MovieMediaDTO;
import com.app.module.movie.entity.MovieMedia;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieMediaService {
    Flux<MovieMedia> findAllByMovieId(Long movieId);
    Flux<MovieMedia> findAllByMovieIdAndQuality(Long movieId,String quality);
    Mono<MovieMedia> findByMovieIdAndEpisode(Long movieId,byte episode);
    Mono<MovieMedia> save(MovieMediaDTO movieMediaDTO);
    Mono<MovieMedia> update(Long id,MovieMediaDTO movieMediaDTO);
    Mono<Void> delete(Long id);
}
