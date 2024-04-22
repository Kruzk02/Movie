package com.app.Router;

import com.app.Handler.MovieHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class MovieRouter {

    private final MovieHandler movieHandler;

    @Autowired
    public MovieRouter(MovieHandler movieHandler) {
        this.movieHandler = movieHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> movieRoutes(){
        return RouterFunctions
                .route(GET("/movies"),movieHandler::getAllMovie)
                .andRoute(GET("/movies/{id}"),movieHandler::getById)
                .andRoute(POST("/movies"),movieHandler::create)
                .andRoute(PUT("/movies/{id}"),movieHandler::update)
                .andRoute(DELETE("/movies/{id}"),movieHandler::delete);
    }
}
