package com.app.Repository;

import com.app.Entity.MovieRatingPK;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MovieRatingPkRepository extends ReactiveCrudRepository<MovieRatingPK,Long> {
}
