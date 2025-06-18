package com.app.module.actor.service.impl;

import com.app.module.actor.dto.ActorDTO;
import com.app.module.actor.entity.Actor;
import com.app.type.Nationality;
import com.app.Expection.ActorNotFound;
import com.app.module.actor.mapper.ActorMapper;
import com.app.module.actor.repository.ActorRepository;
import com.app.module.actor.service.ActorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import static com.app.constants.AppConstants.ACTOR_PHOTO;

@Service
public class ActorServiceImpl implements ActorService {

    private static final Logger log = LogManager.getLogger(ActorServiceImpl.class);
    private final ActorRepository actorRepository;
    private final ReactiveRedisTemplate<String,Actor> redisTemplate;

    @Autowired
    public ActorServiceImpl(ActorRepository actorRepository, ReactiveRedisTemplate<String, Actor> redisTemplate) {
        this.actorRepository = actorRepository;
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
                    .thenReturn(actor))
            )
            .log("Find a Actor with a id: " + id);
    }

    @Override
    public Mono<Actor> save(ActorDTO actorDTO) {
        Actor actor = ActorMapper.INSTANCE.mapActorDtoToActor(actorDTO);
        return actorRepository.save(actor)
                .flatMap(savedActor -> redisTemplate
                        .opsForValue()
                        .set("actor:" + savedActor.getId(),savedActor,Duration.ofHours(24))
                        .thenReturn(savedActor))
            .log("Saved a new Actor: " + actorDTO.getFirstName() + " "+ actorDTO.getLastName());
    }

    @Override
    public Mono<Actor> update(Long id, ActorDTO actorDTO) {
        return actorRepository.findById(id)
            .switchIfEmpty(Mono.error(new ActorNotFound("Actor Not Found")))
            .flatMap(existingActor ->{
                Path path = Paths.get(ACTOR_PHOTO + existingActor.getPhoto());
                File file = path.toFile();

                if (file.exists() && file.isFile()) {
                    file.delete();
                }

                existingActor.setFirstName(actorDTO.getFirstName());
                existingActor.setLastName(actorDTO.getLastName());
                existingActor.setBirthDate(actorDTO.getBirthDate());
                existingActor.setNationality(Enum.valueOf(Nationality.class,actorDTO.getNationality()));
                existingActor.setPhoto(actorDTO.getPhoto());

                return actorRepository.save(existingActor)
                    .flatMap(updatedActor -> redisTemplate
                        .opsForValue()
                        .set("actor:" + updatedActor.getId(),updatedActor,Duration.ofHours(24))
                        .thenReturn(updatedActor));
            })
            .log("Update a Actor with a id: "+id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return actorRepository.findById(id)
            .switchIfEmpty(Mono.error(new ActorNotFound("Actor not found with id: "+ id)))
            .flatMap(actor -> {
                Path path = Paths.get(ACTOR_PHOTO + actor.getPhoto());
                File file = path.toFile();
                if (file.exists() && file.isFile()) {
                    file.delete();
                }

                return actorRepository.delete(actor)
                        .then(redisTemplate.opsForValue().delete("actor:" + actor.getId()));
            })
            .log("Delete a Actor with a id: "+id).then();
    }
}
