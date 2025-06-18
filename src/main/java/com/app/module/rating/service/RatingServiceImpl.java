package com.app.module.rating.service;

import com.app.module.rating.dto.RatingDTO;
import com.app.module.rating.entity.Rating;
import com.app.Expection.*;
import com.app.module.rating.mapper.RatingMapper;
import com.app.module.rating.repository.RatingRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;

@Service
@AllArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final ReactiveRedisTemplate<String,Rating> ratingRedisTemplate;
    private final ReactiveRedisTemplate<String,Double> doubleRedisTemplate;

    @Override
    public Mono<Double> findAvgRatingByMovieId(Long movieId) {
        return doubleRedisTemplate.opsForValue().get("avgRatting::movieId::" + movieId)
            .switchIfEmpty(ratingRepository.findAvgRatingByMovieId(movieId)
                .flatMap(avgRating -> doubleRedisTemplate.opsForValue()
                    .set("avgRatting::movieId::" + movieId,avgRating,Duration.ofHours(2))
                    .thenReturn(avgRating)
                )
            );
    }

    @Override
    public Mono<Rating> findRatingByMovieIdAndUserId(Long movieId, Long userId) {
         return ratingRedisTemplate.opsForValue().get("rating::movieId::" + movieId + "::userId::" + userId)
             .switchIfEmpty(ratingRepository.findRatingByMovieIdAndUserId(movieId,userId)
                 .flatMap(rating -> ratingRedisTemplate.opsForValue()
                     .set("rating::movieId::" + movieId + "::userId::" + userId,rating,Duration.ofHours(2))
                     .thenReturn(rating)
                 )
             );
    }

    @Override
    public Mono<Rating> save(RatingDTO ratingDTO,Long userId) {
        Rating rating = RatingMapper.INSTANCE.mappingDtoToEntity(ratingDTO);
        rating.setUserId(userId);

        return ratingRepository.findRatingByMovieIdAndUserId(rating.getMovieId(),rating.getUserId())
            .switchIfEmpty(ratingRepository.save(rating)
                .flatMap(savedRating -> ratingRedisTemplate.opsForValue()
                    .set("rating::movieId::" + savedRating.getMovieId() + "::userId::" + savedRating.getUserId(),savedRating,Duration.ofHours(2))
                    .thenReturn(savedRating)
                )
            ).onErrorResume(e -> Mono.error(new RatingSameMovieException("Hey, you can't do that, mate.")));
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
            })
            .flatMap(updatedRating -> ratingRedisTemplate.opsForValue()
                    .set("rating::movieId::" + updatedRating.getMovieId() + "::userId::" + updatedRating.getUserId(),updatedRating,Duration.ofHours(2))
                    .thenReturn(updatedRating));
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
