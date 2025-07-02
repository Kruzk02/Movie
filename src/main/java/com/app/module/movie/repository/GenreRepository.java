package com.app.module.movie.repository;

import com.app.module.movie.entity.Genre;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface GenreRepository extends ReactiveCrudRepository<Genre,Long> {
}
