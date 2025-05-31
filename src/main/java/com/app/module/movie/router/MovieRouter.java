package com.app.module.movie.router;

import com.app.module.movie.handler.MovieMediaHandler;
import com.app.module.movie.handler.MovieHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;

@AllArgsConstructor
public class MovieRouter {

    private final MovieHandler movieHandler;
    private final MovieMediaHandler movieMediaHandler;

    /**
     * Configures the router functions for movie-related endpoints.
     *
     * @return A RouterFunction that routes request to the appropriate handler methods.
     */
    @Bean
    public RouterFunction<ServerResponse> movieRouter(){
        return RouterFunctions
                .route(GET("/movies"),movieHandler::findAll)
                .andRoute(GET("/movies/{id}"),movieHandler::findById)
                .andRoute(GET("/movies/{id}/poster"),movieHandler::getMoviePoster)
                .andRoute(POST("/movies"),movieHandler::save)
                .andRoute(PUT("/movies/{id}"),movieHandler::update)
                .andRoute(DELETE("/movies/{id}"),movieHandler::delete);
    }

    /**
     * Configures the router functions for movie media-related endpoints.
     * @return A RouterFunction that routes requests to the appropriate handler method.
     */
    @Bean
    public RouterFunction<ServerResponse> movieMediaRouterApi(){
        return RouterFunctions
                .route(GET("/movie_media/{movieId}"),movieMediaHandler::findAllByMovieId)
                .andRoute(GET("/movie_media/movies/{movieId}/{quality}"),movieMediaHandler::findAllByMovieIdAndQuality)
                .andRoute(GET("/movie_media/movies/{movieId}/{episode}"),movieMediaHandler::findByMovieIdAndEpisode)
                .andRoute(GET("/movie_media/video/{filename}"),movieMediaHandler::streamVideo)
                .andRoute(POST("/movie_media"),movieMediaHandler::save)
                .andRoute(PUT("/movie_media/{id}"), movieMediaHandler::update)
                .andRoute(DELETE("/movie_media/{id}"),movieMediaHandler::delete);
    }
}
