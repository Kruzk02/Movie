package com.app.Repository;

import com.app.Entity.UserRolePK;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRoleRepository extends ReactiveCrudRepository<UserRolePK,Long> {
}
