package com.app.Router;

import com.app.Handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class Router {

    private final DirectorHandler directorHandler;
    private final ActorHandler actorHandler;
    private final MovieHandler movieHandler;
    private final RatingHandler ratingHandler;
    private final GenreHandler genreHandler;

    @Autowired
    public Router(DirectorHandler directorHandler, ActorHandler actorHandler, MovieHandler movieHandler, RatingHandler ratingHandler, GenreHandler genreHandler) {
        this.directorHandler = directorHandler;
        this.actorHandler = actorHandler;
        this.movieHandler = movieHandler;
        this.ratingHandler = ratingHandler;
        this.genreHandler = genreHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> directorRouter(){
        return RouterFunctions
                .route(GET("/directors"),directorHandler::findAll)
                .andRoute(GET("/directors/{id}"),directorHandler::findById)
                .andRoute(POST("/directors"),directorHandler::create)
                .andRoute(PUT("/directors/{id}"),directorHandler::update)
                .andRoute(DELETE("/directors/{id}"),directorHandler::delete);
    }

    @Bean
    public RouterFunction<ServerResponse> actorRouter(){
        return RouterFunctions
                .route(GET("/actors"),actorHandler::findAll)
                .andRoute(GET("/actors/{id}"),actorHandler::findById)
                .andRoute(POST("/actors"),actorHandler::save)
                .andRoute(PUT("/actors/{id}"),actorHandler::update)
                .andRoute(DELETE("/actors/{id}"),actorHandler::delete);
    }

    @Bean
    public RouterFunction<ServerResponse> movieRouter(){
        return RouterFunctions
                .route(GET("/movies"),movieHandler::findAll)
                .andRoute(GET("/movies/{id}"),movieHandler::findById)
                .andRoute(POST("/movies"),movieHandler::save)
                .andRoute(PUT("/movies/{id}"),movieHandler::update)
                .andRoute(DELETE("/movies/{id}"),movieHandler::delete);
    }

    @Bean
    public RouterFunction<ServerResponse> ratingRouter(){
        return RouterFunctions
                .route(GET("/ratings"),ratingHandler::findAll)
                .andRoute(GET("/ratings/{id}"),ratingHandler::findById)
                .andRoute(POST("/ratings"),ratingHandler::saveMovieRating);
    }

    @Bean
    public RouterFunction<ServerResponse> genreRouter(){
        return RouterFunctions
                .route(GET("/genres"),genreHandler::findAll)
                .andRoute(GET("/genres/{id}"),genreHandler::findById);
    }
}
