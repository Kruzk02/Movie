package com.app.Service.Impl;

import com.app.DTO.RatingDTO;
import com.app.Entity.Rating;
import com.app.Expection.MovieNotMatching;
import com.app.Expection.UserNotFound;
import com.app.Expection.UserNotMatching;
import com.app.Expection.RatingNotFound;
import com.app.Mapper.RatingMapper;
import com.app.Repository.RatingRepository;
import com.app.Service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public Mono<Rating> findById(Long id) {
        return ratingRepository.findById(id)
                .switchIfEmpty(Mono.error(new RatingNotFound("Rating not found with a id: " + id)));
    }

    @Override
    public Flux<Rating> findAllByMovieId(Long movieId) {
        return ratingRepository.findAllByMovieId(movieId);
    }

    @Override
    public Mono<Rating> save(RatingDTO ratingDTO,Long userId) {
        Rating rating = RatingMapper.INSTANCE.mappingDtoToEntity(ratingDTO);
        rating.setUserId(userId);
        return ratingRepository.save(rating);
    }

    @Override
    public Mono<Rating> update(Long id,RatingDTO ratingDTO,Long userId) {
        return ratingRepository.findById(id)
            .flatMap(existingRating -> {
                if (!Objects.equals(existingRating.getMovieId(),ratingDTO.getMovieId())){
                    return Mono.error(new MovieNotMatching("Movie not matching"));
                }
                if (!Objects.equals(existingRating.getUserId(),userId)){
                    return Mono.error(new UserNotMatching("User not matching"));
                }
                existingRating.setRating(ratingDTO.getRating());
                existingRating.setUserId(userId);
                existingRating.setMovieId(ratingDTO.getMovieId());
                return ratingRepository.save(existingRating);
            });
    }

    @Override
    public Mono<Void> delete(Long id,Long userId) {
        return ratingRepository.findById(id)
            .switchIfEmpty(Mono.error(new RatingNotFound("Rating not found with a id: " + id)))
            .flatMap(rating -> {
                if (!Objects.equals(rating.getUserId(),userId)){
                    return Mono.error(new UserNotFound("User not matching"));
                }
                return ratingRepository.delete(rating);
            });
    }
}
