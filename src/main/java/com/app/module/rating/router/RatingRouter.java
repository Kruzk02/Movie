package com.app.module.rating.router;

import com.app.module.rating.handler.RatingHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;

@AllArgsConstructor
public class RatingRouter {

    private final RatingHandler ratingHandler;

    /**
     * Configures the router functions for rating-related endpoints.
     *
     * @return A RouterFunction that routes request to the appropriate handler methods.
     */
    @Bean
    public RouterFunction<ServerResponse> ratingRouter(){
        return RouterFunctions
                .route(GET("/ratings/movies/{movieId}"),ratingHandler::findAvgRatingByMovieId)
                .andRoute(GET("/ratings/movies/{movieId}/users"),ratingHandler::findRatingByMovieIdAndUserId)
                .andRoute(POST("/ratings"),ratingHandler::save)
                .andRoute(PUT("/ratings/{id}"),ratingHandler::update)
                .andRoute(DELETE("/ratings/{id}"),ratingHandler::delete);
    }
}
