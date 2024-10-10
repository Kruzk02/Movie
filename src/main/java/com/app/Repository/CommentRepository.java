package com.app.Repository;

import com.app.Entity.Comment;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CommentRepository extends ReactiveCrudRepository<Comment, Long> {
    @Query("SELECT id, movie_id, content, create_atFROM comments WHERE user_id = :userId LIMIT :limit OFFSET :offset")
    Flux<Comment> findAllByUserId(Long userId, int limit, int offset);

    @Query("SELECT id, user_id, content, create_at FROM comments WHERE movie_id = :movieId LIMIT :limit OFFSET :offset")
    Flux<Comment> findAllByMovieId(Long movieId, int limit, int offset);
}
