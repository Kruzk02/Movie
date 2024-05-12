package com.app.Repository;

import com.app.Entity.MovieRatingPK;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieRatingPkRepository extends ReactiveCrudRepository<MovieRatingPK,Long> {

    @Query("SELECT * From movie_rating where movie_id = :movieId")
    Flux<MovieRatingPK> findAllByMovieId(Long movieId);

    @Modifying
    @Query("DELETE FROM movie_rating where movie_id = :movieId")
    Mono<Void> deleteByMovieId(Long movieId);
}
