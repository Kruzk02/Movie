package com.app.Service.Impl;

import com.app.DTO.DirectorMovieDTO;
import com.app.Entity.Director;
import com.app.Entity.DirectorMoviePK;
import com.app.Entity.Movie;
import com.app.Repository.DirectorMovieRepository;
import com.app.Service.DirectorMovieService;
import com.app.messaging.processor.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DirectorMovieServiceImpl implements DirectorMovieService {

    private final DirectorMovieRepository directorMovieRepository;
    private final Processor<Movie> movieProcessor;

    @Autowired
    public DirectorMovieServiceImpl(DirectorMovieRepository directorMovieRepository, Processor<Movie> movieProcessor) {
        this.directorMovieRepository = directorMovieRepository;
        this.movieProcessor = movieProcessor;
    }

    @Override
    public Mono<DirectorMoviePK> saveDirectorMovie(DirectorMovieDTO directorMovieDTO) {
        return movieProcessor.getOneData()
                .flatMap(movie ->
                        directorMovieRepository.save(new DirectorMoviePK(directorMovieDTO.getDirectorId(),movie.getId()))
                                .onErrorResume(error -> Mono.error(new RuntimeException("Failed to save director movie", error)))
                );
    }

    @Override
    public Flux<Director> findDirectorByMovieId(Long movieId) {
        return directorMovieRepository.findDirectorByMovieId(movieId)
                .log("Find director by movie id: " + movieId);
    }

    @Override
    public Flux<Movie> findMovieByDirector(Long id) {
        return directorMovieRepository.findMovieByDirectorId(id)
                .log("Find movie by director id: " + id);
    }
}
