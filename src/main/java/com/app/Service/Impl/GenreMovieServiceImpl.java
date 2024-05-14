package com.app.Service.Impl;

import com.app.DTO.GenreMovieDTO;
import com.app.Entity.GenreMoviePK;
import com.app.Mapper.GenreMovieMapper;
import com.app.Repository.GenreMovieRepository;
import com.app.Service.GenreMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GenreMovieServiceImpl implements GenreMovieService {

    private final GenreMovieRepository genreMovieRepository;

    @Autowired
    public GenreMovieServiceImpl(GenreMovieRepository genreMovieRepository) {
        this.genreMovieRepository = genreMovieRepository;
    }

    @Override
    public Mono<GenreMoviePK> save(GenreMovieDTO genreMovieDTO) {
        return genreMovieRepository.save(GenreMovieMapper.INSTANCE.mappingDtoToEntity(genreMovieDTO));
    }
}
