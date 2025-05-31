package com.app.module.genre.router;

import com.app.module.genre.handler.GenreHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@AllArgsConstructor
public class GenreRouter {

    private final GenreHandler genreHandler;

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
