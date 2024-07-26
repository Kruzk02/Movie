package com.app.Service.Impl;

import com.app.DTO.ActorDTO;
import com.app.Entity.Actor;
import com.app.Entity.Nationality;
import com.app.Expection.ActorNotFound;
import com.app.Mapper.ActorMapper;
import com.app.Repository.ActorRepository;
import com.app.Service.ActorService;
import com.app.Service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class ActorServiceImpl implements ActorService {

    private final ActorRepository actorRepository;
    private final ReactiveRedisTemplate<String,Actor> redisTemplate;
    private final FileService fileService;

    @Autowired
    public ActorServiceImpl(ActorRepository actorRepository, ReactiveRedisTemplate<String, Actor> redisTemplate, FileService fileService) {
        this.actorRepository = actorRepository;
        this.redisTemplate = redisTemplate;
        this.fileService = fileService;
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
                    .thenReturn(actor))
            )
            .log("Find a Actor with a id: " + id);
    }

    @Override
    public Mono<Actor> save(ActorDTO actorDTO, FilePart filePart,String filename) {
        return fileService.save(filePart,"actorPhoto",filename)
            .then(Mono.fromCallable(() -> {
                Actor actor = ActorMapper.INSTANCE.mapActorDtoToActor(actorDTO);
                actor.setPhoto(filename);
                return actor;
            }))
            .flatMap(actorRepository::save)
            .log("Saved a new Actor: " + actorDTO.getFirstName() + " "+ actorDTO.getLastName());
    }

    @Override
    public Mono<Actor> update(Long id, ActorDTO actorDTO, FilePart filePart,String filename) {
        return actorRepository.findById(id)
            .switchIfEmpty(Mono.error(new ActorNotFound("Actor Not Found")))
            .flatMap(existingActor ->{
                existingActor.setFirstName(actorDTO.getFirstName());
                existingActor.setLastName(actorDTO.getLastName());
                existingActor.setBirthDate(actorDTO.getBirthDate());
                existingActor.setNationality(Enum.valueOf(Nationality.class,actorDTO.getNationality()));

                return fileService.delete("actorPhoto",existingActor.getPhoto())
                    .then(fileService.save(filePart,"actorPhoto",filename)
                        .then(Mono.fromCallable(() -> {
                            existingActor.setPhoto(filename);
                            return existingActor;
                        }))
                        .flatMap(actorRepository::save)
                    );
            })
            .log("Update a Actor with a id: "+id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return actorRepository.findById(id)
            .switchIfEmpty(Mono.error(new ActorNotFound("Actor not found with id: "+ id)))
            .flatMap(actor -> {
                fileService.delete("actorPhoto",actor.getPhoto());
                return actorRepository.delete(actor);
            })
            .log("Delete a Actor with a id: "+id);
    }
}
