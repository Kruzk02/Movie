package com.app.Service.Impl;

import com.app.DTO.DirectorDTO;
import com.app.Entity.Director;
import com.app.Entity.Nationality;
import com.app.Expection.DirectorNotFound;
import com.app.Mapper.DirectorMapper;
import com.app.Repository.DirectorRepository;
import com.app.Service.DirectorService;
import com.app.Service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class DirectorServiceImpl implements DirectorService {

    private final DirectorRepository directorRepository;
    private final FileService fileService;
    private final ReactiveRedisTemplate<String,Director> redisTemplate;

    @Autowired
    public DirectorServiceImpl(DirectorRepository directorRepository, FileService fileService, ReactiveRedisTemplate<String, Director> redisTemplate) {
        this.directorRepository = directorRepository;
        this.fileService = fileService;
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
    public Mono<Director> save(DirectorDTO directorDTO, FilePart photo, String filename) {
        return fileService.save(photo,"directorPhoto",filename)
                .then(Mono.fromCallable(() -> {
                    Director director = DirectorMapper.INSTANCE.mapDirectorDtoToDirector(directorDTO);
                    director.setPhoto(filename);
                    return director;
                }))
                .flatMap(directorRepository::save)
                .log("Save a new Director: " + directorDTO);
    }

    @Override
    public Mono<Director> update(Long id, DirectorDTO directorDTO, FilePart photo,String filename) {
        return directorRepository.findById(id)
            .switchIfEmpty(Mono.error(new DirectorNotFound("Director Not Found with a id: " + id)))
            .flatMap(existingDirector ->{
                existingDirector.setFirstName(directorDTO.getFirstName());
                existingDirector.setLastName(directorDTO.getLastName());
                existingDirector.setBirthDate(directorDTO.getBirthDate());
                existingDirector.setNationality(Enum.valueOf(Nationality.class,directorDTO.getNationality()));

                return fileService.delete("directorPhoto",existingDirector.getPhoto())
                    .then(fileService.save(photo,"directorPhoto",filename)
                        .then(Mono.fromCallable(() -> {
                            existingDirector.setPhoto(filename);
                            return existingDirector;
                        }))
                        .flatMap(directorRepository::save)
                    );
            })
            .log("Update a director with a id: " + id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return directorRepository.findById(id)
            .switchIfEmpty(Mono.error(new DirectorNotFound("Director Not Found with a id: " + id)))
            .flatMap(director -> {
                fileService.delete("directorPhoto",director.getPhoto());
                return directorRepository.delete(director);
            })
            .log("Delete a director with a id: " + id);
    }
}
