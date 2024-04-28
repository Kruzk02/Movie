package com.app.Router;

import com.app.Handler.ActorHandler;
import com.app.Handler.DirectorHandler;
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

    @Autowired
    public Router(DirectorHandler directorHandler, ActorHandler actorHandler) {
        this.directorHandler = directorHandler;
        this.actorHandler = actorHandler;
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
}
