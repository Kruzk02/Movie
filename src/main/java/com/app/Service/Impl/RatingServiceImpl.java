package com.app.Service.Impl;

import com.app.DTO.RatingDTO;
import com.app.Entity.Rating;
import com.app.Repository.RatingRepository;
import com.app.Service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public Flux<Rating> findAll() {
        return ratingRepository.findAll();
    }

    @Override
    public Mono<Rating> findById(Long id) {
        return ratingRepository.findById(id);
    }

    // update and delete method return null because those use for mapping movie_rating table.

    @Override
    public Mono<Rating> update(Long id, RatingDTO ratingDTO) {
        return null;
    }

    @Override
    public Mono<Void> delete(Long id) {
        return null;
    }
}
