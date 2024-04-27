package com.app.Service.Impl;

import com.app.DTO.ActorDTO;
import com.app.Entity.Actor;
import com.app.Entity.Nationality;
import com.app.Expection.ActorNotFound;
import com.app.Mapper.ActorMapper;
import com.app.Repository.ActorRepository;
import com.app.Service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ActorServiceImpl implements ActorService {
    private final ActorRepository actorRepository;

    @Autowired
    public ActorServiceImpl(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Override
    public Flux<Actor> findAll() {
        return actorRepository.findAll().log("Find all Actor");
    }

    @Override
    public Mono<Actor> findById(Long id) {
        return actorRepository.findById(id)
                .switchIfEmpty(Mono.error(new ActorNotFound("Actor Not Found")))
                .log("Find a Actor with a id: "+id);
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
                .switchIfEmpty(Mono.error(new ActorNotFound("Actor Not Found")))
                .flatMap(actorRepository::delete)
                .log("Delete a Actor with a id: "+id);
    }
}
