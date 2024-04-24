package com.app.Service.Impl;

import com.app.DTO.DirectorDTO;
import com.app.Entity.Director;
import com.app.Entity.Nationality;
import com.app.Expection.DirectorNotFound;
import com.app.Mapper.DirectorMapper;
import com.app.Repository.DirectorRepository;
import com.app.Service.DirectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DirectorServiceImpl implements DirectorService {

    private final DirectorRepository directorRepository;

    @Autowired
    public DirectorServiceImpl(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    @Override
    public Flux<Director> findAll() {
        return directorRepository.findAll();
    }

    @Override
    public Mono<Director> findById(Long id) {
        return directorRepository.findById(id)
                .switchIfEmpty(Mono.error(new DirectorNotFound("Director Not Found")));
    }

    @Override
    public Mono<Director> save(DirectorDTO directorDTO) {
        Director director = DirectorMapper.INSTANCE.mapDirectorDtoToDirector(directorDTO);
        return directorRepository.save(director);
    }

    @Override
    public Mono<Director> update(Long id, DirectorDTO directorDTO) {
        return directorRepository.findById(id)
                .switchIfEmpty(Mono.error(new DirectorNotFound("Director Not Found")))
                .flatMap(existingDirector ->{
                    existingDirector.setFirstName(directorDTO.getFirstName());
                    existingDirector.setLastName(directorDTO.getLastName());
                    existingDirector.setBirthDate(directorDTO.getBirthDate());
                    existingDirector.setNationality(Enum.valueOf(Nationality.class,directorDTO.getNationality()));

                    return directorRepository.save(existingDirector);
                });
    }

    @Override
    public Mono<Void> delete(Long id) {
        return directorRepository.findById(id)
                .switchIfEmpty(Mono.error(new DirectorNotFound("Director Not Found")))
                .flatMap(directorRepository::delete);
    }
}
