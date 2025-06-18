package com.app.module.director.repository;

import com.app.module.director.entity.Director;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface DirectorRepository extends ReactiveCrudRepository<Director,Long> {
}
