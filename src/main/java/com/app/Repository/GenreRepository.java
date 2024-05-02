package com.app.Repository;

import com.app.Entity.Genre;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface GenreRepository extends ReactiveCrudRepository<Genre,Long> {
    Mono<Genre> getGenreByMovieId(Long id);
}
