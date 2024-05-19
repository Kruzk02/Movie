package com.app.Repository;

import com.app.Entity.MovieRatingPK;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface MovieRatingPkRepository extends ReactiveCrudRepository<MovieRatingPK,Long> {

    @Query("SELECT * From movie_rating mr JOIN rating r ON mr.rating_id = r.id where mr.movie_id = :movieId")
    Flux<MovieRatingPK> findAllByMovieId(Long movieId);
}
