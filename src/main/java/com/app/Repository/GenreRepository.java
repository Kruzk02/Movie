package com.app.Repository;

import com.app.Entity.Genre;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface GenreRepository extends ReactiveCrudRepository<Genre,Long> {
}
