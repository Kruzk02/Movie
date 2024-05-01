package com.app.Repository;

import com.app.Entity.Movie;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MovieRepository extends ReactiveCrudRepository<Movie,Long> {
}
