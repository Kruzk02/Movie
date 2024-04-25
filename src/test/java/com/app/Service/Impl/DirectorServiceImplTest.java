package com.app.Service.Impl;

import com.app.DTO.DirectorDTO;
import com.app.Entity.Director;
import com.app.Entity.Nationality;
import com.app.Expection.DirectorNotFound;
import com.app.Mapper.DirectorMapper;
import com.app.Repository.DirectorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DirectorServiceImplTest {

    @Mock
    private DirectorRepository directorRepository;

    @InjectMocks
    private DirectorServiceImpl directorService;

    private DirectorDTO directorDTO;
    @BeforeEach
    void setUp() {
        directorDTO = new DirectorDTO();
        directorDTO.setFirstName("first");
        directorDTO.setLastName("last");
        directorDTO.setBirthDate(LocalDate.ofYearDay(2002,24));
        directorDTO.setNationality(String.valueOf(Nationality.USA));
    }

    @Test
    void findAll() {
        Director director1 = DirectorMapper.INSTANCE.mapDirectorDtoToDirector(directorDTO);
        Director director2 = DirectorMapper.INSTANCE.mapDirectorDtoToDirector(directorDTO);
        when(directorRepository.findAll()).thenReturn(Flux.just(director1, director2));

        StepVerifier.create(directorService.findAll())
                .expectNext(director1)
                .expectNext(director2)
                .verifyComplete();
    }

    @Test
    void findById_ExistingId_ReturnsDirector() {
        Director director = DirectorMapper.INSTANCE.mapDirectorDtoToDirector(directorDTO);

        when(directorRepository.findById(1L)).thenReturn(Mono.just(director));

        StepVerifier.create(directorService.findById(1L))
                .expectNext(director)
                .verifyComplete();
    }

    @Test
    void findById_NonExistingId_ReturnsError() {
        Long id = 1L;
        when(directorRepository.findById(id)).thenReturn(Mono.empty());
        StepVerifier.create(directorService.findById(id))
                .expectError(DirectorNotFound.class)
                .verify();
    }

    @Test
    void save() {
        Director director = DirectorMapper.INSTANCE.mapDirectorDtoToDirector(directorDTO);
        when(directorRepository.save(any(Director.class))).thenReturn(Mono.just(director));

        StepVerifier.create(directorService.save(directorDTO))
                .expectNext(director)
                .verifyComplete();
    }
}
