package com.app.Service;

import com.app.DTO.MovieRatingDTO;
import com.app.Entity.MovieRatingPK;
import reactor.core.publisher.Mono;

public interface MovieRatingService {
    Mono<MovieRatingPK> save(MovieRatingDTO movieRatingDTO);
}
