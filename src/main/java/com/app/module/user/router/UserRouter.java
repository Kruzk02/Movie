package com.app.module.user.router;

import com.app.module.user.handler.UserHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
@AllArgsConstructor
public class UserRouter {

    private final UserHandler userHandler;

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
}
