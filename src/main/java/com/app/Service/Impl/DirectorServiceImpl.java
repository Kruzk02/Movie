package com.app.Service.Impl;

import com.app.DTO.DirectorDTO;
import com.app.DTO.DirectorMovieDTO;
import com.app.Entity.Director;
import com.app.Entity.DirectorMoviePK;
import com.app.Entity.Movie;
import com.app.Entity.Nationality;
import com.app.Expection.DirectorNotFound;
import com.app.Mapper.DirectorMapper;
import com.app.Repository.DirectorMovieRepository;
import com.app.Repository.DirectorRepository;
import com.app.Service.DirectorService;
import com.app.messaging.processor.Processor;
import com.app.messaging.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class DirectorServiceImpl implements DirectorService {

    private final DirectorRepository directorRepository;
    private final DirectorMovieRepository directorMovieRepository;
    private final Producer<Director> directorProducer;
    private final Processor<Movie> movieProcessor;
    private final ReactiveRedisTemplate<String,Director> redisTemplate;

    @Autowired
    public DirectorServiceImpl(DirectorRepository directorRepository, DirectorMovieRepository directorMovieRepository, Producer<Director> directorProducer, Processor<Movie> movieProcessor, ReactiveRedisTemplate<String, Director> redisTemplate) {
        this.directorRepository = directorRepository;
        this.directorMovieRepository = directorMovieRepository;
        this.directorProducer = directorProducer;
        this.movieProcessor = movieProcessor;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Flux<Director> findAll() {
        return redisTemplate.keys("director:*")
            .flatMap(key -> redisTemplate.opsForValue().get(key))
            .thenMany(directorRepository.findAll()
                .flatMap(director ->
                    redisTemplate
                    .opsForValue()
                    .set("director:" + director.getId(),director, Duration.ofHours(24))
                    .thenReturn(director)))
            .log("Find all directors");
    }

    @Override
    public Mono<Director> findById(Long id) {
        return redisTemplate.opsForValue().get("director:"+ id)
            .switchIfEmpty(directorRepository.findById(id)
                .switchIfEmpty(Mono.error(new DirectorNotFound("Director not found with a id: " + id)))
                .flatMap(director ->
                    redisTemplate
                    .opsForValue()
                    .set("director:" + director.getId(),director, Duration.ofHours(24))
                    .thenReturn(director)))
            .log("Find a Director with a id: " + id);
    }

    @Override
    public Mono<Director> save(DirectorDTO directorDTO) {
        Director director = DirectorMapper.INSTANCE.mapDirectorDtoToDirector(directorDTO);
        return directorRepository.save(director);
    }

    @Override
    public Mono<Director> update(Long id, DirectorDTO directorDTO) {
        return directorRepository.findById(id)
            .switchIfEmpty(Mono.error(new DirectorNotFound("Director Not Found with a id: " + id)))
            .flatMap(existingDirector ->{
                existingDirector.setFirstName(directorDTO.getFirstName());
                existingDirector.setLastName(directorDTO.getLastName());
                existingDirector.setBirthDate(directorDTO.getBirthDate());
                existingDirector.setNationality(Enum.valueOf(Nationality.class,directorDTO.getNationality()));

                return directorRepository.save(existingDirector);
            })
            .log("Update a director with a id: " + id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return directorRepository.findById(id)
            .switchIfEmpty(Mono.error(new DirectorNotFound("Director Not Found with a id: " + id)))
            .flatMap(directorRepository::delete)
            .log("Delete a director with a id: " + id);
    }

    @Override
    public Mono<DirectorMoviePK> saveDirectorMovie(DirectorMovieDTO directorMovieDTO) {
        return movieProcessor.getOneData()
            .flatMap(movie ->
                directorMovieRepository.save(new DirectorMoviePK(directorMovieDTO.getDirectorId(),movie.getId()))
                .onErrorResume(error -> Mono.error(new RuntimeException("Failed to save director movie", error)))
            );
    }

    @Override
    public Flux<Director> findDirectorByMovieId(Long movieId) {
        return directorMovieRepository.findDirectorByMovieId(movieId)
            .doOnNext(directorProducer::send)
            .log("Find director by movie id: " + movieId);
    }
}
