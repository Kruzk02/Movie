package com.app.Handler;

import com.app.DTO.DirectorDTO;
import com.app.DTO.DirectorMovieDTO;
import com.app.Entity.Director;
import com.app.Entity.Movie;
import com.app.Service.DirectorMovieService;
import com.app.Service.DirectorService;
import com.app.messaging.processor.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class DirectorHandler {

    private final DirectorMovieService directorMovieService;
    private final DirectorService directorService;

    @Autowired
    public DirectorHandler(DirectorMovieService directorMovieService, DirectorService directorService) {
        this.directorMovieService = directorMovieService;
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

    public Mono<ServerResponse> saveDirectorMovie(ServerRequest request){
        return request.bodyToMono(DirectorMovieDTO.class)
            .flatMap(directorMovieService::saveDirectorMovie)
                .flatMap(savedDirectorMovie -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedDirectorMovie))
                .onErrorResume(error -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("Error saving director: " + error.getMessage()));
    }

    public Mono<ServerResponse> findMovieByDirectorId(ServerRequest request){
        Long id = Long.valueOf(request.pathVariable("id"));
        Flux<Movie> movieFlux = directorMovieService.findMovieByDirector(id);
        return movieFlux.collectList()
            .flatMap(movies -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(movies))
            .switchIfEmpty(ServerResponse.notFound().build());
    }
}
