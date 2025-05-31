package com.app.module.rating.repository;

import com.app.module.rating.entity.Rating;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RatingRepository extends ReactiveCrudRepository<Rating,Long> {
    @Query("SELECT CAST(AVG(rating) AS DOUBLE PRECISION) from ratings WHERE movie_id =:movieId")
    Mono<Double> findAvgRatingByMovieId(Long movieId);

    @Query("SELECT id,rating FROM ratings WHERE movie_id =:movieId AND user_id =:userId")
    Mono<Rating> findRatingByMovieIdAndUserId(Long movieId,Long userId);

}
