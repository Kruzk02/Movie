package com.app.Service;

import com.app.DTO.MovieRatingDTO;
import com.app.DTO.RatingDTO;
import com.app.Entity.MovieRatingPK;
import com.app.Entity.Rating;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RatingService {

    Flux<Rating> findAll();
    Mono<Rating> findById(Long id);
    Mono<MovieRatingPK> saveMovieRating (MovieRatingDTO MovieRatingDTO);
    Mono<MovieRatingPK> update(RatingDTO ratingDTO);
    Mono<Void> delete(Long id);
}
