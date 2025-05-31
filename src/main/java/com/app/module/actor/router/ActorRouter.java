package com.app.module.actor.router;

import com.app.module.actor.handler.ActorHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;

@AllArgsConstructor
public class ActorRouter {

    private final ActorHandler actorHandler;

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
}
