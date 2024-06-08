package com.app.Service.Impl;

import com.app.DTO.ActorDTO;
import com.app.DTO.ActorMovieDTO;
import com.app.Entity.Actor;
import com.app.Entity.ActorMoviePK;
import com.app.Entity.Movie;
import com.app.Entity.Nationality;
import com.app.Expection.ActorNotFound;
import com.app.Mapper.ActorMapper;
import com.app.Repository.ActorMovieRepository;
import com.app.Repository.ActorRepository;
import com.app.Service.ActorService;
import com.app.messaging.processor.Processor;
import com.app.messaging.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class ActorServiceImpl implements ActorService {
    private final ActorRepository actorRepository;
    private final ActorMovieRepository actorMovieRepository;
    private final Producer<Actor> actorProducer;
    private final Processor<Movie> movieProcessor;
    private final ReactiveRedisTemplate<String,Actor> redisTemplate;

    @Autowired
    public ActorServiceImpl(ActorRepository actorRepository, ActorMovieRepository actorMovieRepository, Producer<Actor> actorProducer, Processor<Movie> movieProcessor, ReactiveRedisTemplate<String, Actor> redisTemplate) {
        this.actorRepository = actorRepository;
        this.actorMovieRepository = actorMovieRepository;
        this.actorProducer = actorProducer;
        this.movieProcessor = movieProcessor;
        this.redisTemplate = redisTemplate;
    }


    @Override
    public Flux<Actor> findAll() {
        return redisTemplate.keys("actor:*")
            .flatMap(key -> redisTemplate.opsForValue().get(key))
            .thenMany(actorRepository.findAll()
                .flatMap(actor ->
                    redisTemplate
                    .opsForValue()
                    .set("actor:" + actor.getId(),actor, Duration.ofHours(24))
                    .thenReturn(actor)))
            .log("Find all Actor");
    }

    @Override
    public Mono<Actor> findById(Long id) {
        return redisTemplate.opsForValue().get("actor:"+ id)
            .switchIfEmpty(actorRepository.findById(id)
                .switchIfEmpty(Mono.error(new ActorNotFound("Actor not found with a id: " + id)))
                .flatMap(actor ->
                    redisTemplate
                    .opsForValue()
                    .set("actor:" + actor.getId(),actor, Duration.ofHours(24))
                    .thenReturn(actor)))
            .log("Find a Actor with a id: " + id);
    }

    @Override
    public Mono<Actor> save(ActorDTO actorDTO) {
        Actor actor = ActorMapper.INSTANCE.mapActorDtoToActor(actorDTO);
        return actorRepository.save(actor)
                .log("Saved a new Actor: "+ actor);
    }

    @Override
    public Mono<Actor> update(Long id, ActorDTO actorDTO) {
        return actorRepository.findById(id)
            .switchIfEmpty(Mono.error(new ActorNotFound("Actor Not Found")))
            .flatMap(existingActor ->{
                existingActor.setFirstName(actorDTO.getFirstName());
                existingActor.setLastName(actorDTO.getLastName());
                existingActor.setBirthDate(actorDTO.getBirthDate());
                existingActor.setNationality(Enum.valueOf(Nationality.class,actorDTO.getNationality()));
                return actorRepository.save(existingActor);
            })
            .log("Update a Actor with a id: "+id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return actorRepository.findById(id)
            .switchIfEmpty(Mono.error(new ActorNotFound("Actor not found with id: "+ id)))
            .flatMap(actorRepository::delete)
            .log("Delete a Actor with a id: "+id);
    }

    @Override
    public Mono<ActorMoviePK> saveActorMovie(ActorMovieDTO actorMovieDTO) {
        return movieProcessor.getOneData()
            .flatMap(movie ->
                actorMovieRepository.save(new ActorMoviePK(actorMovieDTO.getActorId(),movie.getId()))
                .onErrorResume(error -> Mono.error(new RuntimeException("Failed to save actor movie", error)))
            );
    }

    @Override
    public Flux<Actor> findActorByMovieId(Long movieId) {
        return actorMovieRepository.findActorByMovieId(movieId)
                .doOnNext(actorProducer::send);
    }
}
