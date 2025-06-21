package com.app.module.director.service.impl;

import com.app.module.director.dto.DirectorDTO;
import com.app.module.director.entity.Director;
import com.app.type.Nationality;
import com.app.Expection.DirectorNotFound;
import com.app.module.director.mapper.DirectorMapper;
import com.app.module.director.repository.DirectorRepository;
import com.app.module.director.service.DirectorService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

@Service
public class DirectorServiceImpl implements DirectorService {

    private final DirectorRepository directorRepository;
    private final ReactiveRedisTemplate<String,Director> redisTemplate;

    @Autowired
    public DirectorServiceImpl(DirectorRepository directorRepository, ReactiveRedisTemplate<String, Director> redisTemplate) {
        this.directorRepository = directorRepository;
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

        var filePart = directorDTO.photo();
        int lastIndexOfDot = filePart.filename().lastIndexOf('.');
        String extension = "";
        if (lastIndexOfDot != 1) {
            extension = filePart.filename().substring(lastIndexOfDot);
        }

        String filename = RandomStringUtils.randomAlphabetic(15);
        filename += extension.replaceAll("[(){}]", "");

        Path path = Paths.get("directorPhoto/" + filename);

        director.setPhoto(filename);

        return filePart.transferTo(path).then(
            directorRepository.save(director)
                .flatMap(savedDirector -> redisTemplate
                    .opsForValue()
                    .set("director:" + savedDirector.getId(),savedDirector,Duration.ofHours(24))
                    .thenReturn(savedDirector))
                .log("Save a new Director: " + directorDTO)
        );
    }

    @Override
    public Mono<Director> update(Long id, DirectorDTO directorDTO) {
        return directorRepository.findById(id)
            .switchIfEmpty(Mono.error(new DirectorNotFound("Director Not Found with a id: " + id)))
            .flatMap(existingDirector ->{

                Path path = Paths.get("directorPhoto/" + existingDirector.getPhoto());
                File file = path.toFile();

                if (file.exists() && file.isFile()) {
                    file.delete();
                }

                var filePart = directorDTO.photo();

                int lastIndexOfDot = filePart.filename().lastIndexOf('.');
                String extension = "";
                if (lastIndexOfDot != 1) {
                    extension = filePart.filename().substring(lastIndexOfDot);
                }

                String filename = RandomStringUtils.randomAlphabetic(15);
                filename += extension.replaceAll("[(){}]", "");
                Path newPath = Paths.get("directorPhoto/" + filename);


                existingDirector.setFirstName(directorDTO.firstName());
                existingDirector.setLastName(directorDTO.lastName());
                existingDirector.setBirthDate(directorDTO.birthDate());
                existingDirector.setNationality(Enum.valueOf(Nationality.class,directorDTO.nationality()));
                existingDirector.setPhoto(filename);

                return filePart.transferTo(newPath).then(
                    directorRepository.save(existingDirector)
                       .flatMap(updatedDirector -> redisTemplate
                           .opsForValue()
                           .set("director:" + updatedDirector.getId(),updatedDirector,Duration.ofHours(24))
                           .thenReturn(updatedDirector))
                );
            })
            .log("Update a director with a id: " + id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return directorRepository.findById(id)
            .switchIfEmpty(Mono.error(new DirectorNotFound("Director Not Found with a id: " + id)))
            .flatMap(director -> {

                Path path = Paths.get("directorPhoto/" + director.getPhoto());
                File file = path.toFile();

                if (file.exists() && file.isFile()) {
                    file.delete();
                }

                return directorRepository.delete(director)
                        .then(redisTemplate.opsForValue().delete("director:" + director.getId()));
            })
            .log("Delete a director with a id: " + id).then();
    }
}
