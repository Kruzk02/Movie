package com.app.Repository;

import com.app.Entity.Director;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface DirectorRepository extends ReactiveCrudRepository<Director,Long> {
}
