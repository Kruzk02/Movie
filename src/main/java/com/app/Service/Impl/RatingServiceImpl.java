package com.app.Service.Impl;

import com.app.DTO.MovieRatingDTO;
import com.app.DTO.RatingDTO;
import com.app.Entity.MovieRatingPK;
import com.app.Entity.Rating;
import com.app.Expection.RatingNotFound;
import com.app.Mapper.MovieRatingMapper;
import com.app.Mapper.RatingMapper;
import com.app.Repository.MovieRatingPkRepository;
import com.app.Repository.RatingRepository;
import com.app.Service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final MovieRatingPkRepository movieRatingPkRepository;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository,MovieRatingPkRepository movieRatingPkRepository) {
        this.ratingRepository = ratingRepository;
        this.movieRatingPkRepository = movieRatingPkRepository;
    }

    @Override
    public Flux<Rating> findAll() {
        return ratingRepository.findAll();
    }

    @Override
    public Mono<Rating> findById(Long id) {
        return ratingRepository.findById(id)
                .switchIfEmpty(Mono.error(new RatingNotFound("Rating not found with a id: " + id)));
    }

    @Override
    @Transactional
    public Mono<MovieRatingPK> saveMovieRating (MovieRatingDTO MovieRatingDTO) {
        MovieRatingPK movieRatingPK = MovieRatingMapper.INSTANCE.mappingDtoToEntity(MovieRatingDTO);
        return movieRatingPkRepository.save(movieRatingPK);
    }

    @Override
    public Mono<MovieRatingPK> update(RatingDTO ratingDTO) {
        return null;
    }

    @Override
    public Mono<Void> delete(Long id) {
        return null;
    }
}
