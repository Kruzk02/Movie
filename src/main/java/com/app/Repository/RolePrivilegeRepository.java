package com.app.Repository;

import com.app.Entity.Privilege;
import com.app.Entity.RolePrivilegePK;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface RolePrivilegeRepository extends ReactiveCrudRepository<RolePrivilegePK,Long> {
    @Query("SELECT p.* FROM role_privilege rp " +
            "JOIN privilege p ON rp.privilege_id = p.id " +
            "JOIN role r ON rp.role_id = r.id " +
            "WHERE r.name = :name")
    Flux<Privilege> findPrivilegeByRole(String name);
}
