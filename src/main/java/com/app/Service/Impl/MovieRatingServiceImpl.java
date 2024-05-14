package com.app.Service.Impl;

import com.app.DTO.MovieRatingDTO;
import com.app.Entity.MovieRatingPK;
import com.app.Expection.MovieNotFound;
import com.app.Mapper.MovieRatingMapper;
import com.app.Repository.MovieRatingPkRepository;
import com.app.Service.MovieRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MovieRatingServiceImpl implements MovieRatingService {

    private final MovieRatingPkRepository movieRatingPkRepository;

    @Autowired
    public MovieRatingServiceImpl(MovieRatingPkRepository movieRatingPkRepository) {
        this.movieRatingPkRepository = movieRatingPkRepository;
    }

    @Override
    public Mono<MovieRatingPK> save(MovieRatingDTO movieRatingDTO) {
        return movieRatingPkRepository.save(MovieRatingMapper.INSTANCE.mappingDtoToEntity(movieRatingDTO));
    }
}
