package com.app.Service.Impl;

import com.app.DTO.RatingDTO;
import com.app.Entity.Movie;
import com.app.Entity.MovieRatingPK;
import com.app.Entity.Rating;
import com.app.Expection.RatingNotFound;
import com.app.Mapper.RatingMapper;
import com.app.Repository.MovieRatingPkRepository;
import com.app.Repository.MovieRepository;
import com.app.Repository.RatingRepository;
import com.app.Service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final MovieRatingPkRepository movieRatingPkRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository, MovieRatingPkRepository movieRatingPkRepository, MovieRepository movieRepository) {
        this.ratingRepository = ratingRepository;
        this.movieRatingPkRepository = movieRatingPkRepository;
        this.movieRepository = movieRepository;
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
    public Mono<MovieRatingPK> save(RatingDTO ratingDTO) {
        Rating rating = RatingMapper.INSTANCE.mappingDtoToEntity(ratingDTO);

        MovieRatingPK movieRatingPK = new MovieRatingPK();
        movieRatingPK.setRatingId(rating.getId());
        movieRatingPK.setMovieId(ratingDTO.getMovieId());

        return movieRatingPkRepository.save(movieRatingPK);
    }

    @Override
    public Mono<Double> getAverageRating(Long movieId){
        Flux<MovieRatingPK> movieRatingPKFlux = movieRatingPkRepository.findAllByMovieId(movieId);

        Flux<Double> ratingsFlux = movieRatingPKFlux
                .flatMap(movieRatingPK -> ratingRepository.findById(movieRatingPK.getRatingId()))
                .map(Rating::getRating);

        return ratingsFlux.collect(Collectors.averagingDouble(Double::doubleValue));
    }
}
