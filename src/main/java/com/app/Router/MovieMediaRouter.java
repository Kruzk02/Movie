package com.app.Router;

import com.app.Handler.MovieMediaHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;

@Configuration
public class MovieMediaRouter {

    private final MovieMediaHandler movieMediaHandler;

    @Autowired
    public MovieMediaRouter(MovieMediaHandler movieMediaHandler) {
        this.movieMediaHandler = movieMediaHandler;
    }

    @Bean()
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
