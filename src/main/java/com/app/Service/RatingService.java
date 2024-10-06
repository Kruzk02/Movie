package com.app.Service;

import com.app.DTO.RatingDTO;
import com.app.Entity.Rating;
import reactor.core.publisher.Mono;

public interface RatingService {
    Mono<Double> findAvgRatingByMovieId(Long movieId);
    Mono<Rating> findRatingByMovieIdAndUserId(Long movieId,Long userId);
    Mono<Rating> save(RatingDTO ratingDTO,Long userId);
    Mono<Rating> update(Long id,RatingDTO ratingDTO,Long userId);
    Mono<Void> delete(Long id,Long userId);
}
