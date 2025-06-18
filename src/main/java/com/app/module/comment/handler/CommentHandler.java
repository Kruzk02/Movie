package com.app.module.comment.handler;

import com.app.module.comment.dto.CommentDTO;
import com.app.module.comment.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class CommentHandler {

    private final CommentService commentService;

    public Mono<ServerResponse> findAllByUserId(ServerRequest request) {
        Long userId = request.exchange().getAttribute("userId");
        int limit = Integer.parseInt(request.queryParam("limit").orElse("10"));
        int offset = Integer.parseInt(request.queryParam("offset").orElse("0"));

        return commentService.findAllByUserId(userId,limit,offset)
                .collectList()
                .flatMap(comments -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(comments))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> findALlByMovieId(ServerRequest request) {
        Long movieId = Long.valueOf(request.pathVariable("movieId"));
        int limit = Integer.parseInt(request.queryParam("limit").orElse("10"));
        int offset = Integer.parseInt(request.queryParam("offset").orElse("0"));

        return commentService.findAllByMovieId(movieId,limit,offset)
            .collectList()
            .flatMap(comment -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(comment))
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));

        return commentService.findById(id)
            .flatMap(comment -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(comment))
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        Long userId = request.exchange().getAttribute("userId");
        return request.bodyToMono(CommentDTO.class)
                .flatMap(commentDTO -> {
                    commentDTO.setUserId(userId);
                    return commentService.save(commentDTO);
                })
                .flatMap(comment -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(comment))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue("Error saving comment"));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        Long userId = request.exchange().getAttribute("userId");

        return request.bodyToMono(CommentDTO.class)
            .flatMap(commentDTO -> {
                commentDTO.setUserId(userId);
                return commentService.updateById(id,userId,commentDTO);
            })
            .flatMap(comment -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(comment))
            .onErrorResume(e -> ServerResponse.badRequest().bodyValue("Error update comment"));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        Long userId = request.exchange().getAttribute("userId");
        String role = request.exchange().getAttribute("role");

        return commentService.deleteById(id,userId,role)
            .then(ServerResponse.ok().build())
            .switchIfEmpty(ServerResponse.notFound().build())
            .onErrorResume(e -> ServerResponse.badRequest().bodyValue("Error delete comment"));
    }

}
