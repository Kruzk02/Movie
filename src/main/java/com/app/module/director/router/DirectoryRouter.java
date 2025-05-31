package com.app.module.director.router;

import com.app.module.director.handler.DirectorHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;

@AllArgsConstructor
public class DirectoryRouter {

    private final DirectorHandler directorHandler;

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
}
