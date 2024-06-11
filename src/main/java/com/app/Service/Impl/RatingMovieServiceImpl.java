package com.app.Service.Impl;

import com.app.DTO.RatingDTO;
import com.app.Entity.MovieRatingPK;
import com.app.Entity.Rating;
import com.app.Mapper.RatingMapper;
import com.app.Repository.RatingMovieRepository;
import com.app.Repository.RatingRepository;
import com.app.Service.RatingMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
public class RatingMovieServiceImpl implements RatingMovieService {

    private final RatingMovieRepository ratingMovieRepository;
    private final RatingRepository ratingRepository;
    @Autowired
    public RatingMovieServiceImpl(RatingMovieRepository ratingMovieRepository, RatingRepository ratingRepository) {
        this.ratingMovieRepository = ratingMovieRepository;
        this.ratingRepository = ratingRepository;
    }

    @Override
    public Mono<MovieRatingPK> save(RatingDTO ratingDTO) {
        Rating rating = RatingMapper.INSTANCE.mappingDtoToEntity(ratingDTO);

        MovieRatingPK movieRatingPK = new MovieRatingPK();
        movieRatingPK.setRatingId(rating.getId());
        movieRatingPK.setMovieId(ratingDTO.getMovieId());

        return ratingMovieRepository.save(movieRatingPK);
    }

    @Override
    public Mono<Double> getAverageRating(Long movieId){
        Flux<MovieRatingPK> movieRatingPKFlux = ratingMovieRepository.findAllByMovieId(movieId);

        Flux<Double> ratingsFlux = movieRatingPKFlux
                .flatMap(movieRatingPK -> ratingRepository.findById(movieRatingPK.getRatingId()))
                .map(Rating::getRating);

        return ratingsFlux.collect(Collectors.averagingDouble(Double::doubleValue));
    }
}
