package com.app.Service;

import com.app.Entity.Rating;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RatingService {
    Flux<Rating> findAll();
    Mono<Rating> findById(Long id);
}
