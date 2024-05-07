package com.app.Service.Impl;

import com.app.DTO.MovieDTO;
import com.app.Entity.Genre;
import com.app.Entity.Movie;
import com.app.Entity.Rating;
import com.app.Expection.GenreNotFound;
import com.app.Expection.MovieNotFound;
import com.app.Expection.RatingNotFound;
import com.app.Mapper.MovieMapper;
import com.app.Repository.GenreRepository;
import com.app.Repository.MovieRepository;
import com.app.Repository.RatingRepository;
import com.app.Service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final RatingRepository ratingRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, GenreRepository genreRepository, RatingRepository ratingRepository) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.ratingRepository = ratingRepository;
    }

    @Override
    public Flux<Movie> findAll() {
        return movieRepository.findAll()
                .log("Find all movie.")
                .flatMap(movie -> {
                    Movie basicMovie = new Movie();
                    basicMovie.setId(movie.getId());
                    basicMovie.setTitle(movie.getTitle());
                    basicMovie.setRelease_year(movie.getRelease_year());
                    basicMovie.setMovie_length(movie.getMovie_length());
                    return Mono.just(basicMovie);
                });
    }

    @Override
    public Flux<Movie> findByGenreId(Long genreId) {
        return movieRepository.findByGenreId(genreId)
                .log("Find movie with a genre id: " + genreId);
    }

    @Override
    public Mono<Movie> findById(Long id) {
        return movieRepository.findById(id)
                .switchIfEmpty(Mono.error(new MovieNotFound("Movie Not Found")))
                .flatMap(movie -> {
                    Mono<Genre> genreMono = genreRepository.findById(movie.getGenre_id())
                            .switchIfEmpty(Mono.error(new GenreNotFound("Genre Not Found For Movie")));
                    Mono<Rating> ratingMono = ratingRepository.findById(movie.getRating_id())
                            .switchIfEmpty(Mono.error(new RatingNotFound("Rating Not Found For Movie")));
                    return Mono.zip(Mono.just(movie), genreMono, ratingMono)
                            .map(tuple -> {
                                Movie foundMovie = tuple.getT1();
                                Genre genre = tuple.getT2();
                                Rating rating = tuple.getT3();
                                foundMovie.setGenres(Collections.singleton(genre));
                                foundMovie.setRatings(Collections.singleton(rating));
                                return foundMovie;
                            });
                })
                .log("Find movie with id: " + id);
    }

    @Override
    public Mono<Movie> save(MovieDTO movieDTO) {
        Movie movie = MovieMapper.INSTANCE.mapDtoToEntity(movieDTO);
        return movieRepository.save(movie)
                .log("Save a new movie");
    }

    @Override
    public Mono<Movie> update(Long id, MovieDTO movieDTO) {
        return movieRepository.findById(id)
                .switchIfEmpty(Mono.error(new MovieNotFound("Movie Not Found")))
                .flatMap(existingMovie -> {
                    existingMovie.setTitle(movieDTO.getTitle());
                    existingMovie.setRelease_year(movieDTO.getRelease_year());
                    existingMovie.setMovie_length(movieDTO.getMovie_length());

                    Mono<Rating> ratingMono = ratingRepository.findById(movieDTO.getRating_id())
                            .switchIfEmpty(Mono.error(new RatingNotFound("Rating Not Found For Movie " + id)));

                    Mono<Genre> genreMono = genreRepository.findById(movieDTO.getGenre_id())
                            .switchIfEmpty(Mono.error(new GenreNotFound("Genre Not Found For Movie " + id)));

                    return Mono.zip(ratingMono, genreMono)
                            .flatMap(tuple -> {
                                existingMovie.setRatings(Collections.singleton(tuple.getT1()));
                                existingMovie.setGenres(Collections.singleton(tuple.getT2()));
                                return movieRepository.save(existingMovie);
                            });
                })
                .log("Update a Movie with a id: " + id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return movieRepository.findById(id)
                .switchIfEmpty(Mono.error(new MovieNotFound("Movie Not Found")))
                .flatMap(movieRepository::delete)
                .log("Delete a movie with a id: " + id);
    }
}
