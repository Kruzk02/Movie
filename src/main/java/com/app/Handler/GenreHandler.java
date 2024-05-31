package com.app.Handler;

import com.app.DTO.GenreDTO;
import com.app.Entity.Genre;
import com.app.Service.GenreService;
import com.app.messaging.processor.MovieProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class GenreHandler {

    private final GenreService genreService;
    private final MovieProcessor movieProcessor;

    @Autowired
    public GenreHandler(GenreService genreService, MovieProcessor movieProcessor) {
        this.genreService = genreService;
        this.movieProcessor = movieProcessor;
    }

    public Mono<ServerResponse> findAll(ServerRequest request){
        return ServerResponse.ok().body(genreService.findAll(), Genre.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request){
        Long id = Long.valueOf(request.pathVariable("id"));
        Mono<Genre> genreMono = genreService.findById(id);
        return genreMono.flatMap(genre -> ServerResponse.ok().bodyValue(genre))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest request){
        return request.bodyToMono(GenreDTO.class)
                .flatMap(genreDTO -> movieProcessor.getMovie()
                        .flatMap(movie -> genreService.save(genreDTO.getGenreId(),movie.getId()))
                        .flatMap(savedGenreMovie -> ServerResponse.ok().bodyValue(savedGenreMovie))
                        .switchIfEmpty(ServerResponse.notFound().build())
                        .onErrorResume(error -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("Error saving genre: " + error.getMessage())));
    }
}
