package com.app.Router;

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

    @Autowired
    public Router(DirectorHandler directorHandler) {
        this.directorHandler = directorHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunction(){
        return RouterFunctions
                .route(GET("/directors"),directorHandler::findAll)
                .andRoute(GET("/directors/{id}"),directorHandler::findById)
                .andRoute(POST("/directors"),directorHandler::create)
                .andRoute(PUT("/directors/{id}"),directorHandler::update)
                .andRoute(DELETE("/directors/{id}"),directorHandler::delete);
    }
}
