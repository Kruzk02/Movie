package com.app.Service;

import com.app.DTO.DirectorDTO;
import com.app.Entity.Director;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DirectorService {
    Flux<Director> findAll();
    Mono<Director> findById(Long id);
    Mono<Director> save(DirectorDTO directorDTO);
    Mono<Director> update(Long id,DirectorDTO directorDTO);
    Mono<Void> delete(Long id);
}
