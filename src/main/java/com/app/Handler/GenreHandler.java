package com.app.Handler;

import com.app.DTO.GenreMovieDTO;
import com.app.Entity.Genre;
import com.app.Service.GenreMovieService;
import com.app.Service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class GenreHandler {

    private final GenreService genreService;
    private final GenreMovieService genreMovieService;

    @Autowired
    public GenreHandler(GenreService genreService, GenreMovieService genreMovieService) {
        this.genreService = genreService;
        this.genreMovieService = genreMovieService;
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

    public Mono<ServerResponse> saveGenreMovie(ServerRequest request){
        Mono<GenreMovieDTO> genreMovieDTOMono = request.bodyToMono(GenreMovieDTO.class);
        return genreMovieDTOMono.flatMap(genreMovieDTO -> genreMovieService.save(genreMovieDTO)
                .flatMap(saved -> ServerResponse.ok().bodyValue(saved)));
    }
}
