package com.app.Router;

import com.app.Handler.*;
import com.app.module.actor.handler.ActorHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
@AllArgsConstructor
public class Router {

    private final DirectorHandler directorHandler;

    private final RatingHandler ratingHandler;
    private final GenreHandler genreHandler;
    private final CommentHandler commentHandler;

    /**
     * Configures the router functions for director-related endpoints.
     *
     * @return A RouterFunction that routes request to the appropriate handler methods.
     */
    @Bean
    public RouterFunction<ServerResponse> directorRouter(){
        return RouterFunctions
                .route(GET("/directors"),directorHandler::findAll)
                .andRoute(GET("/directors/{id}"),directorHandler::findById)
                .andRoute(GET("/directors/{id}/photo"),directorHandler::getDirectorPhoto)
                .andRoute(GET("/directors/{id}/movies"),directorHandler::findMovieByDirectorId)
                .andRoute(POST("/directors/movies"),directorHandler::saveDirectorMovie)
                .andRoute(PUT("directors/movies"), directorHandler::updateDirectorMovie)
                .andRoute(POST("/directors"),directorHandler::create)
                .andRoute(PUT("/directors/{id}"),directorHandler::update)
                .andRoute(DELETE("/directors/{id}"),directorHandler::delete);
    }

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

    /**
     * Configures the router functions for genre-related endpoints.
     *
     * @return A RouterFunction that routes requests to the appropriate handler methods.
     */
    @Bean
    public RouterFunction<ServerResponse> genreRouter(){
        return RouterFunctions
                .route(GET("/genres"),genreHandler::findAll)
                .andRoute(GET("/genres/{id}"),genreHandler::findById)
                .andRoute(GET("/genres/{id}/movies"),genreHandler::findMovieByGenreId)
                .andRoute(POST("/genres/movies"),genreHandler::saveGenreMovie)
                .andRoute(PUT("/genres/movies"),genreHandler::updateGenreMovie);
    }

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
