package com.app.module.comment.service;

import com.app.module.comment.dto.CommentDTO;
import com.app.module.comment.entity.Comment;
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
