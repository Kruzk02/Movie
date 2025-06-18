package com.app.module.comment.service;

import com.app.module.comment.dto.CommentDTO;
import com.app.module.comment.entity.Comment;
import com.app.Expection.CommentNotFoundException;
import com.app.Expection.CommentNotMatchException;
import com.app.module.comment.mapper.CommentMapper;
import com.app.module.comment.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ReactiveRedisTemplate<String,Comment> redisTemplate;

    @Override
    public Flux<Comment> findAllByUserId(Long userId, int limit, int offset) {
        return redisTemplate.opsForList().range("userComments:" + userId,offset,offset + limit - 1)
            .switchIfEmpty(commentRepository.findAllByUserId(userId,limit,offset)
                .collectList()
                .flatMapMany(comments -> {
                    if (!comments.isEmpty()) {
                        return redisTemplate.opsForList().rightPushAll("userComments:" + userId,comments)
                            .thenMany(Flux.fromIterable(comments));
                    }
                    return Flux.fromIterable(comments);
                })
            );
    }

    @Override
    public Flux<Comment> findAllByMovieId(Long movieId,int limit, int offset) {
        return redisTemplate.opsForList().range("movieComments:" + movieId,offset,offset + limit - 1)
            .switchIfEmpty(commentRepository.findAllByMovieId(movieId, limit, offset)
                .collectList()
                .flatMapMany(comments -> {
                    if (!comments.isEmpty()) {
                        return redisTemplate.opsForList().rightPushAll("movieComments:" + movieId,comments)
                            .thenMany(Flux.fromIterable(comments));
                    }
                    return Flux.fromIterable(comments);
                })
            );
    }

    @Override
    public Mono<Comment> findById(Long id) {
        return redisTemplate.opsForValue().get("comment:" + id)
            .switchIfEmpty(commentRepository.findById(id)
                .switchIfEmpty(Mono.error(new CommentNotFoundException("Comment not found with a id:" + id)))
                .flatMap(comment ->
                    redisTemplate
                    .opsForValue()
                    .set("comment:" + comment.getId(),comment,Duration.ofHours(2))
                    .thenReturn(comment)
                )
            ).log("Find a comment with a id: " + id);
    }

    @Override
    public Mono<Comment> save(CommentDTO commentDTO) {
        Comment comment = CommentMapper.INSTANCE.mapCommentDTOToComment(commentDTO);
        return commentRepository.save(comment)
            .flatMap(savedComment -> redisTemplate
                .opsForValue()
                .set("comment:" + savedComment.getId(),savedComment,Duration.ofHours(2))
                .thenReturn(savedComment)
            ).log("Save a new comment");
    }

    @Override
    public Mono<Comment> updateById(Long id,Long userId, CommentDTO commentDTO) {
        return commentRepository.findById(id)
            .switchIfEmpty(Mono.error(new CommentNotFoundException("Comment not found with a id:" + id)))
            .flatMap(exstingComment -> {
                if (Objects.equals(exstingComment.getUserId(),userId)) {
                    exstingComment.setContent(commentDTO.getContent());
                    exstingComment.setCreateAt(LocalDateTime.now());

                    return commentRepository.save(exstingComment)
                        .flatMap(updatedComment -> redisTemplate
                            .opsForValue()
                            .set("comment:" + updatedComment.getId(), updatedComment, Duration.ofHours(2))
                            .thenReturn(updatedComment)
                        );
                } else {
                    throw new CommentNotMatchException("Comment not match with a user");
                }
            }).log("Update a comment with a id: " + id);
    }

    @Override
    public Mono<Void> deleteById(Long id,Long userId,String role) {
        return commentRepository.findById(id)
            .switchIfEmpty(Mono.error(new CommentNotFoundException("Comment not found with a id:" + id)))
            .flatMap(comment -> {
                if (Objects.equals(comment.getUserId(),userId) || Objects.equals(role,"ROLE_ADMIN")) {
                    return commentRepository.delete(comment)
                            .then(redisTemplate.opsForValue().delete("comment:" + comment.getId()));
                } else {
                    throw new CommentNotMatchException("Comment not match with user");
                }
            })
            .log("Delete a comment with a id: " + id).then();
    }
}
