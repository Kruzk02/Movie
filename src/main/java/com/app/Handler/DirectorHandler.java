package com.app.Handler;

import com.app.DTO.DirectorDTO;
import com.app.Entity.Director;
import com.app.Service.DirectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class DirectorHandler {

    private final DirectorService directorService;

    @Autowired
    public DirectorHandler(DirectorService directorService) {
        this.directorService = directorService;
    }

    public Mono<ServerResponse> findAll(ServerRequest request){
        Flux<Director> directors = directorService.findAll();
        return ServerResponse.ok().body(directors, Director.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request){
        Long id = Long.valueOf(request.pathVariable("id"));
        Mono<Director> directorMono = directorService.findById(id);
        return directorMono.flatMap(director -> ServerResponse.ok().bodyValue(director)
                .switchIfEmpty(ServerResponse.notFound().build()));
    }

    public Mono<ServerResponse> create(ServerRequest request){
        Mono<DirectorDTO> directorMono = request.bodyToMono(DirectorDTO.class);
        return directorMono.flatMap(directorDTO -> directorService.save(directorDTO)
                .flatMap(savedDirector -> ServerResponse.ok().bodyValue(savedDirector)));
    }

    public Mono<ServerResponse> update(ServerRequest request){
        Long id = Long.valueOf(request.pathVariable("id"));
        Mono<DirectorDTO> directorMono = request.bodyToMono(DirectorDTO.class);
        return directorMono.flatMap(directorDTO -> directorService.update(id,directorDTO))
                .flatMap(savedDirector -> ServerResponse.ok().bodyValue(savedDirector))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest request){
        Long id = Long.valueOf(request.pathVariable("id"));
        return directorService.delete(id)
                .then(ServerResponse.ok().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
