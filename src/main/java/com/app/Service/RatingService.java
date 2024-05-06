package com.app.Service;

import com.app.DTO.RatingDTO;
import com.app.Entity.Rating;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RatingService {

    Flux<Rating> findAll();
    Mono<Rating> findById(Long id);
    Mono<Rating> update(Long id,RatingDTO ratingDTO);
    Mono<Void> delete(Long id);
}
