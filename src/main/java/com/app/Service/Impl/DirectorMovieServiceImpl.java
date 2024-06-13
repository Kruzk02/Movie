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

import java.util.Set;
import java.util.stream.Collectors;

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
    public Mono<Void> saveDirectorMovie(DirectorMovieDTO directorMovieDTO) {
        return movieProcessor.getOneData()
            .flatMapMany(movie -> {
                Set<DirectorMoviePK> directorMovies = directorMovieDTO.getDirectorId().stream()
                    .map(directorId -> new DirectorMoviePK(directorId,movie.getId()))
                    .collect(Collectors.toSet());
                return directorMovieRepository.saveAll(directorMovies);
            })
            .then()
            .log("Save director and movie");
    }

    @Override
    public Mono<DirectorMoviePK> updateDirectorMovie(DirectorMovieDTO directorMovieDTO) {
        return null;
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
