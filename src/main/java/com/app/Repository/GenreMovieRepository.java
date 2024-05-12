package com.app.Repository;

import com.app.Entity.GenreMoviePK;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface GenreMovieRepository extends ReactiveCrudRepository<GenreMoviePK,Long> {
}
