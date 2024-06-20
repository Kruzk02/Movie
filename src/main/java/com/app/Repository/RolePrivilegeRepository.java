package com.app.Repository;

import com.app.Entity.RolePrivilegePK;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface RolePrivilegeRepository extends ReactiveCrudRepository<RolePrivilegePK,Long> {
}
