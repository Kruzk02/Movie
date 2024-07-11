package com.app.Service;

import com.app.DTO.RatingDTO;
import com.app.Entity.Rating;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RatingService {
    Mono<Rating> findById(Long id);
    Flux<Rating> findAllByMovieId(Long movieId);
    Mono<Rating> save(RatingDTO ratingDTO,Long userId);
    Mono<Rating> update(Long id,RatingDTO ratingDTO,Long userId);
    Mono<Void> delete(Long id,Long userId);
}
