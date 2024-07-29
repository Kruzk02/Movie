package com.app.Repository;

import com.app.Entity.MovieMedia;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieMediaRepository extends ReactiveCrudRepository<MovieMedia,Long> {

    @Query(value = "SELECT * FROM movie_media WHERE movie_id = :movieId")
    Flux<MovieMedia> findAllByMovieId(@Param("movieId") Long movieId);

    @Query(value = "SELECT * FROM movie_media WHERE movie_id = :movieId AND quality = :quality")
    Flux<MovieMedia> findAllByMovieIdAndQuality(@Param("movieId") Long movieId, @Param("quality") String quality);

    @Query(value = "SELECT * FROM movie_media WHERE movie_id = :movieId AND episode = :episodes")
    Mono<MovieMedia> findByMovieIdAndEpisode(@Param("movieId") Long movieId, @Param("episodes") byte episode);

    @Modifying
    @Transactional
    @Query("UPDATE movie_media SET "+
            "file_path = :#{#movieMedia.filePath}," +
            "episodes =:#{#movieMedia.episodes}," +
            "duration =:#{#movieMedia.duration}," +
            "quality =:#{#movieMedia.quality}," +
            "WHERE movie_id =: movieId")
    Mono<MovieMedia> updateWithMovieId(@Param("movieId") Long movieId,@Param("movieMedia") MovieMedia movieMedia);
}
