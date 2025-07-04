package com.app.Router;

import com.app.module.actor.handler.ActorHandler;
import com.app.module.comment.handler.CommentHandler;
import com.app.module.director.handler.DirectorHandler;
import com.app.module.movie.handler.GenreHandler;
import com.app.module.rating.handler.RatingHandler;
import lombok.AllArgsConstructor;
import com.app.module.movie.handler.MovieHandler;
import com.app.module.movie.handler.MovieMediaHandler;
import com.app.module.user.handler.UserHandler;
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
    private final ActorHandler actorHandler;
    private final MovieHandler movieHandler;
    private final RatingHandler ratingHandler;
    private final GenreHandler genreHandler;
    private final UserHandler userHandler;
    private final CommentHandler commentHandler;
    private final MovieMediaHandler movieMediaHandler;

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
     * Configures the router functions for user-related endpoints.
     * @return A RouterFunction that routes requests to the appropriate handler method.
     */
    @Bean
    public RouterFunction<ServerResponse> usersRouter(){
        return RouterFunctions
                .route(POST("/users/login"),userHandler::login)
                .andRoute(POST("/users/register"),userHandler::register)
                .andRoute(PUT("/users/{id}"),userHandler::update)
                .andRoute(DELETE("/users/{id}"),userHandler::delete)
                .andRoute(GET("/users/verify"),userHandler::verifyAccount)
                .andRoute(GET("/users/profile"),userHandler::getUserProfile);
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
