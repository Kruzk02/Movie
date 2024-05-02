package com.app.Repository;

import com.app.Entity.Rating;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface RatingRepository extends ReactiveCrudRepository<Rating,Long> {
}
