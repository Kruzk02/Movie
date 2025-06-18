package module.movie.repository;

import module.movie.entity.Movie;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface MovieRepository extends ReactiveCrudRepository<Movie,Long> {
    @Query("SELECT * FROM movie WHERE genre_id = :genreId")
    Flux<Movie> findByGenreId(Long genreId);
}
