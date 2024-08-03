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
     * Configures the router functions for actor-related endpoints.
     *
     * @return A RouterFunction that routes request to the appropriate handler methods.
     */
    @Bean
    public RouterFunction<ServerResponse> actorRouter(){
        return RouterFunctions
                .route(GET("/actors"),actorHandler::findAll)
                .andRoute(GET("/actors/{id}"),actorHandler::findById)
                .andRoute(GET("/actors/{id}/photo"),actorHandler::getActorPhoto)
                .andRoute(POST("/actors/movies"),actorHandler::saveActorMovie)
                .andRoute(PUT("/actors/movies"),actorHandler::updateActorMovie)
                .andRoute(GET("/actors/{id}/movies"),actorHandler::findMovieByActorId)
                .andRoute(POST("/actors"),actorHandler::save)
                .andRoute(PUT("/actors/{id}"),actorHandler::update)
                .andRoute(DELETE("/actors/{id}"),actorHandler::delete);
    }

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
     * Configures the router functions for rating-related endpoints.
     *
     * @return A RouterFunction that routes request to the appropriate handler methods.
     */
    @Bean
    public RouterFunction<ServerResponse> ratingRouter(){
        return RouterFunctions
                .route(GET("/ratings/{id}"),ratingHandler::findById)
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
}
