package com.app.Service;

import com.app.DTO.DirectorDTO;
import com.app.Entity.Director;
import com.app.Entity.DirectorMoviePK;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DirectorService {
    Flux<Director> findAll();
    Mono<Director> findById(Long id);
    Mono<Director> save(DirectorDTO directorDTO);
    Mono<Director> update(Long id,DirectorDTO directorDTO);
    Mono<Void> delete(Long id);
    Mono<DirectorMoviePK> saveDirectorMovie(Long directorId, Long movieId);
    Flux<Director> findDirectorByMovieId(Long movieId);

}
