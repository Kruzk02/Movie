package com.app.module.rating.service;

import com.app.module.rating.dto.RatingDTO;
import com.app.module.rating.entity.Rating;
import reactor.core.publisher.Mono;

public interface RatingService {
    Mono<Double> findAvgRatingByMovieId(Long movieId);
    Mono<Rating> findRatingByMovieIdAndUserId(Long movieId,Long userId);
    Mono<Rating> save(RatingDTO ratingDTO,Long userId);
    Mono<Rating> update(Long id,RatingDTO ratingDTO,Long userId);
    Mono<Void> delete(Long id,Long userId);
}
