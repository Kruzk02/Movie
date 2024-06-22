package com.app.Repository;

import com.app.Entity.Role;
import com.app.Entity.UserRolePK;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface UserRoleRepository extends ReactiveCrudRepository<UserRolePK,Long> {

    @Query("SELECT r.* FROM user_role ur " +
            "JOIN role r ON ur.role_id = r.id " +
            "JOIN users u ON ur.user_id = u.id " +
            "WHERE u.username = :username")
    Flux<Role> findRolesByUsername(String username);
}
