package com.app.Repository;

import com.app.Entity.Rating;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface RatingRepository extends ReactiveCrudRepository<Rating,Long> {
    @Query("SELECT id,movie_id,rating from rating WHERE movie_id =:movieId")
    Flux<Rating> findAllByMovieId(Long movieId);
}
