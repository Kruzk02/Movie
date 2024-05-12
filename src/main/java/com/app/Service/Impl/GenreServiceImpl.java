package com.app.Service.Impl;

import com.app.DTO.GenreMovieDTO;
import com.app.Entity.Genre;
import com.app.Entity.GenreMoviePK;
import com.app.Expection.GenreNotFound;
import com.app.Mapper.GenreMovieMapper;
import com.app.Repository.GenreMovieRepository;
import com.app.Repository.GenreRepository;
import com.app.Service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreMovieRepository genreMovieRepository;

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository, GenreMovieRepository genreMovieRepository) {
        this.genreRepository = genreRepository;
        this.genreMovieRepository = genreMovieRepository;
    }

    @Override
    public Flux<Genre> findAll() {
        return genreRepository.findAll().log("Find all genres");
    }

    @Override
    public Mono<Genre> findById(Long id) {
        return genreRepository.findById(id)
                .switchIfEmpty(Mono.error(new GenreNotFound("Genre not found with a id: " + id)))
                .log("Find a genre with a id: " + id);
    }

    @Override
    public Mono<GenreMoviePK> save(GenreMovieDTO genreMovieDTO) {
        GenreMoviePK genreMoviePK = GenreMovieMapper.INSTANCE.mappingDtoToEntity(genreMovieDTO);
        return genreMovieRepository.save(genreMoviePK);
    }

}
