package com.app.Service;

import com.app.DTO.RatingDTO;
import com.app.Entity.MovieRatingPK;
import reactor.core.publisher.Mono;

public interface RatingMovieService {
    Mono<MovieRatingPK> save(RatingDTO ratingDTO);
    Mono<Double> getAverageRating(Long movieId);
}
