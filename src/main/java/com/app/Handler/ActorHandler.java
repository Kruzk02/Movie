package com.app.Handler;

import com.app.DTO.ActorDTO;
import com.app.DTO.ActorMovieDTO;
import com.app.Entity.Actor;
import com.app.Entity.Movie;
import com.app.Service.ActorService;
import com.app.messaging.processor.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ActorHandler {

    private final ActorService actorService;
    private final Processor<Movie> processor;

    @Autowired
    public ActorHandler(ActorService actorService,@Qualifier("MovieProcessor") Processor<Movie> processor) {
        this.actorService = actorService;
        this.processor = processor;
    }

    public Mono<ServerResponse> findAll(ServerRequest request){
        Flux<Actor> actors = actorService.findAll();
        return ServerResponse.ok().body(actors,Actor.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request){
        Long id = Long.valueOf(request.pathVariable("id"));
        Mono<Actor> actorMono = actorService.findById(id);
        return actorMono.flatMap(actor -> ServerResponse.ok().bodyValue(actor)
                .switchIfEmpty(ServerResponse.notFound().build()));
    }

    public Mono<ServerResponse> save(ServerRequest request){
        Mono<ActorDTO> actorDTOMono = request.bodyToMono(ActorDTO.class);
        return actorDTOMono.flatMap(actorDTO -> actorService.save(actorDTO)
                .flatMap(savedActor -> ServerResponse.ok().bodyValue(savedActor)));
    }

    public Mono<ServerResponse> update(ServerRequest request){
        Long id = Long.valueOf(request.pathVariable("id"));
        Mono<ActorDTO> actorDTOMono = request.bodyToMono(ActorDTO.class);
        return actorDTOMono.flatMap(actorDTO -> actorService.update(id,actorDTO))
                .flatMap(savedActor -> ServerResponse.ok().bodyValue(savedActor))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest request){
        Long id = Long.valueOf(request.pathVariable("id"));
        return actorService.delete(id)
                .then(ServerResponse.ok().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> saveActorMovie(ServerRequest request){
        return request.bodyToMono(ActorMovieDTO.class)
                .flatMap(actorMovieDTO -> processor.getMovie()
                        .flatMap(movie -> actorService.saveActorMovie(actorMovieDTO.getActorId(),movie.getId()))
                        .flatMap(savedActorMovie -> ServerResponse.ok().bodyValue(savedActorMovie))
                        .switchIfEmpty(ServerResponse.notFound().build())
                        .onErrorResume(error -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("Error saving actor: " + error.getMessage())));
    }
}
