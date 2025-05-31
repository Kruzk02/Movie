package com.app.module.movie.repository;

import com.app.module.movie.entity.MovieMedia;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalTime;

public interface MovieMediaRepository extends ReactiveCrudRepository<MovieMedia,Long> {

    @Query(value = "SELECT * FROM movie_media WHERE movie_id = :movieId")
    Flux<MovieMedia> findAllByMovieId(@Param("movieId") Long movieId);

    @Query(value = "SELECT * FROM movie_media WHERE movie_id = :movieId AND quality = :quality")
    Flux<MovieMedia> findAllByMovieIdAndQuality(@Param("movieId") Long movieId, @Param("quality") String quality);

    @Query(value = "SELECT * FROM movie_media WHERE movie_id = :movieId AND episode = :episodes")
    Mono<MovieMedia> findByMovieIdAndEpisode(@Param("movieId") Long movieId, @Param("episodes") byte episode);

    @Modifying
    @Transactional
    @Query("UPDATE MovieMedia m SET m.filePath = :filePath, " +
            "m.episodes = :episodes, " +
            "m.duration = :duration, " +
            "m.quality = :quality " +
            "WHERE m.movieId = :movieId")
    Mono<Void> updateWithMovieId(@Param("movieId") Long movieId,
                                 @Param("filePath") String filePath,
                                 @Param("episodes") byte episodes,
                                 @Param("duration") LocalTime duration,
                                 @Param("quality") String quality);

}
