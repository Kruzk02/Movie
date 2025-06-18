package com.app.module.genre.repository;

import com.app.module.genre.entity.Genre;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface GenreRepository extends ReactiveCrudRepository<Genre,Long> {
}
