package com.app.module.comment.router;

import com.app.module.comment.handler.CommentHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;

@AllArgsConstructor
public class CommentRouter {

    private final CommentHandler commentHandler;

    /**
     * Configures the router functions for comment-related endpoints
     * @return A RouterFunction that routes requests to the appropriate handler method.
     */
    @Bean
    public RouterFunction<ServerResponse> CommentRouter() {
        return RouterFunctions
                .route(GET("/comments/users"),commentHandler::findAllByUserId)
                .andRoute(GET("/comments/{movieId}/movies"),commentHandler::findALlByMovieId)
                .andRoute(GET("/comments/{id}"),commentHandler::findById)
                .andRoute(POST("/comments"),commentHandler::save)
                .andRoute(PUT("/comments/{id}"),commentHandler::update)
                .andRoute(DELETE("/comments/{id}"),commentHandler::delete);
    }
}
