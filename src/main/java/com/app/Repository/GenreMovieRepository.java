package com.app.Repository;

import com.app.Entity.GenreMoviePK;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenreMovieRepository extends ReactiveCrudRepository<GenreMoviePK,Long> {
    @Query("SELECT * FROM Genre_Movie WHERE movie_id = :movieId")
    Flux<GenreMoviePK> findAllByMovieId(Long movieId);

    @Modifying
    @Query("DELETE FROM Genre_Movie WHERE movie_id = :movieId")
    Mono<Void> deleteByMovieId(Long movieId);
}
