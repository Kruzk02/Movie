package com.app.Service;

import com.app.DTO.CommentDTO;
import com.app.Entity.Comment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentService {
    Flux<Comment> findAllByUserId(Long userId, int limit, int offset);
    Flux<Comment> findAllByMovieId(Long movieId, int limit, int offset);
    Mono<Comment> findById(Long id);
    Mono<Comment> save(CommentDTO commentDTO);
    Mono<Comment> updateById(Long id,Long userId, CommentDTO commentDTO);
    Mono<Void> deleteById(Long id,Long userId,String role);
}
