package com.app.module.director.service;

import com.app.module.director.dto.DirectorDTO;
import com.app.module.director.entity.Director;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DirectorService {
    Flux<Director> findAll();
    Mono<Director> findById(Long id);
    Mono<Director> save(DirectorDTO directorDTO);
    Mono<Director> update(Long id,DirectorDTO directorDTO);
    Mono<Void> delete(Long id);
}
