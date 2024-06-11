package com.app.Service.Impl;

import com.app.Entity.Rating;
import com.app.Expection.RatingNotFound;
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
        return ratingRepository.findById(id)
                .switchIfEmpty(Mono.error(new RatingNotFound("Rating not found with a id: " + id)));
    }

}
