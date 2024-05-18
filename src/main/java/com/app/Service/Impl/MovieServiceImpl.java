package com.app.Service.Impl;

import com.app.DTO.MovieDTO;
import com.app.Entity.*;
import com.app.Expection.MovieNotFound;
import com.app.Mapper.MovieMapper;
import com.app.Repository.*;
import com.app.Service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {
    
    private final MovieRepository movieRepository;
    private final DirectorRepository directorRepository;
    private final DirectorMovieRepository directorMovieRepository;
    private final GenreRepository genreRepository;
    private final GenreMovieRepository genreMovieRepository;
    private final ReactiveRedisTemplate<String,Movie> redisTemplate;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, DirectorRepository directorRepository, DirectorMovieRepository directorMovieRepository, GenreRepository genreRepository, GenreMovieRepository genreMovieRepository, MovieRatingPkRepository movieRatingPkRepository, ReactiveRedisTemplate<String, Movie> redisTemplate) {
        this.movieRepository = movieRepository;
        this.directorRepository = directorRepository;
        this.directorMovieRepository = directorMovieRepository;
        this.genreRepository = genreRepository;
        this.genreMovieRepository = genreMovieRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Flux<Movie> findAll() {
        return redisTemplate.keys("movie:*")
                .flatMap(key -> redisTemplate.opsForValue().get(key))
                .thenMany(
                        movieRepository.findAll()
                                .flatMap(movie -> {
                                    Mono<Set<Genre>> genresMono = genreMovieRepository.findAllByMovieId(movie.getId())
                                            .collectList()
                                            .map(HashSet::new);
                                    Mono<Set<Director>> directorsMono = directorMovieRepository.findAllByMovieId(movie.getId())
                                            .collectList()
                                            .map(HashSet::new);

                                    return Mono.zip(genresMono,directorsMono)
                                            .flatMap(tuple -> {
                                                Set<Genre> genres = tuple.getT1();
                                                Set<Director> directors = tuple.getT2();
                                                movie.setGenres(genres);
                                                movie.setDirectors(directors);

                                                return redisTemplate
                                                        .opsForValue()
                                                        .set("movie:" + movie.getId(),movie, Duration.ofHours(24))
                                                        .thenReturn(movie);
                                            });
                                })
                )
                .log("Find all movie");
    }

    @Override
    public Flux<Movie> findByGenreId(Long genreId) {
        return redisTemplate.keys("movie:*")
                .flatMap(key -> redisTemplate.opsForValue().get(key)
                        .filter(movie -> movie.getGenres().stream()
                                .anyMatch(genre -> genre.getId().equals(genreId)))
                )
                .switchIfEmpty(
                        genreMovieRepository.findAllMovieByGenreId(genreId)
                                .flatMap(movie -> {

                                    Mono<Set<Genre>> genresMono = genreMovieRepository.findAllByMovieId(movie.getId())
                                            .collectList()
                                            .map(HashSet::new);
                                    Mono<Set<Director>> directorsMono = directorMovieRepository.findAllByMovieId(movie.getId())
                                            .collectList()
                                            .map(HashSet::new);

                                    return Mono.zip(genresMono, directorsMono)
                                            .flatMap(tuple -> {
                                                Set<Genre> genres = tuple.getT1();
                                                Set<Director> directors = tuple.getT2();
                                                movie.setGenres(genres);
                                                movie.setDirectors(directors);

                                                return redisTemplate.opsForValue()
                                                        .set("movie:" + movie.getId(), movie, Duration.ofHours(24))
                                                        .thenReturn(movie);
                                            });
                                })
                )
                .log("Find movie with genre id: " + genreId);
    }

    @Override
    public Mono<Movie> findById(Long id) {
        return redisTemplate.opsForValue().get("movie:" + id)
                .switchIfEmpty(
                        movieRepository.findById(id)
                                .switchIfEmpty(Mono.error(new MovieNotFound("Movie not found with a id: " + id)))
                                .flatMap(movie -> {

                                    Mono<Set<Genre>> genresMono = genreMovieRepository.findAllByMovieId(movie.getId())
                                            .collectList()
                                            .map(HashSet::new);
                                    Mono<Set<Director>> directorsMono = directorMovieRepository.findAllByMovieId(movie.getId())
                                            .collectList()
                                            .map(HashSet::new);

                                    return Mono.zip(genresMono,directorsMono)
                                            .flatMap(tuple -> {
                                                Set<Genre> genres = tuple.getT1();
                                                Set<Director> directors = tuple.getT2();
                                                movie.setGenres(genres);
                                                movie.setDirectors(directors);

                                                return redisTemplate
                                                        .opsForValue()
                                                        .set("movie:" + id,movie, Duration.ofHours(24))
                                                        .thenReturn(movie);
                                            });
                                })
                )
                .log("Find movie with id: " + id);
    }

    @Override
    public Mono<Movie> save(MovieDTO movieDTO) {
        Movie movie = MovieMapper.INSTANCE.mapDtoToEntity(movieDTO);

        Mono<Set<Genre>> genres = genreRepository.findAllById(movieDTO.getGenre_id()).collect(Collectors.toSet());
        Mono<Set<Director>> directors = directorRepository.findAllById(movieDTO.getDirector_id()).collect(Collectors.toSet());

        return Mono.zip(genres,directors)
                .flatMap(tuple -> {
                    Set<Genre> genre = tuple.getT1();
                    Set<Director> director = tuple.getT2();
                    movie.setGenres(genre);
                    movie.setDirectors(director);
                    return movieRepository.save(movie)
                            .flatMap(savedMovie -> {
                                List<GenreMoviePK> genreMovies = movieDTO.getGenre_id().stream()
                                        .map(genreId -> new GenreMoviePK(genreId, savedMovie.getId()))
                                        .collect(Collectors.toList());
                                return genreMovieRepository.saveAll(genreMovies)
                                        .then(Mono.just(savedMovie));
                            })
                            .flatMap(savedMovie -> {
                                List<DirectorMoviePK> directorMovies = movieDTO.getDirector_id().stream()
                                        .map(directorId -> new DirectorMoviePK(directorId, savedMovie.getId()))
                                        .collect(Collectors.toList());
                                return directorMovieRepository.saveAll(directorMovies)
                                        .then(Mono.just(savedMovie));
                            });
                })
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to saved a movie: " + e)))
                .log("Save a new movie");
    }

    @Override
    public Mono<Movie> update(Long id, MovieDTO movieDTO) {
        return movieRepository.findById(id)
                .switchIfEmpty(Mono.error(new MovieNotFound("Movie not found with id: " + id)))
                .flatMap(existingMovie -> {
                    existingMovie.setTitle(movieDTO.getTitle());
                    existingMovie.setReleaseYear(movieDTO.getRelease_year());
                    existingMovie.setMovieLength(movieDTO.getMovie_length());

                    Mono<Set<Genre>> genres = genreRepository.findAllById(movieDTO.getGenre_id())
                            .collectList()
                            .map(HashSet::new);

                    Mono<Set<Director>> directors = directorRepository.findAllById(movieDTO.getDirector_id())
                            .collectList()
                            .map(HashSet::new);

                    return Mono.zip(genres, directors)
                            .flatMap(tuple -> {
                                Set<Genre> genreSet = tuple.getT1();
                                Set<Director> directorSet = tuple.getT2();
                                existingMovie.setGenres(genreSet);
                                existingMovie.setDirectors(directorSet);
                                return movieRepository.save(existingMovie);
                            });
                })
                .log("Update a Movie with id: " + id);
    }


    @Override
    public Mono<Void> delete(Long id) {
        return movieRepository.findById(id)
                .switchIfEmpty(Mono.error(new MovieNotFound("Movie not found with a id: " + id)))
                .flatMap(movieRepository::delete)
                .log("Delete a movie with a id: " + id);
    }
}
