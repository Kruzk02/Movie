package com.app.security;

import com.app.Entity.*;
import com.app.module.user.entity.*;
import com.app.module.user.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class SetupData implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePrivilegeRepository rolePrivilegeRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SetupData(UserRepository userRepository, RoleRepository roleRepository, PrivilegeRepository privilegeRepository, UserRoleRepository userRoleRepository, RolePrivilegeRepository rolePrivilegeRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.userRoleRepository = userRoleRepository;
        this.rolePrivilegeRepository = rolePrivilegeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) return;

        setupPrivilegesAndRoles()
//                .then(setupAdminUser())
                .doOnSuccess(unused -> alreadySetup = true)
                .subscribe();
    }

    private Mono<Void> setupPrivilegesAndRoles() {
        return Flux.concat(
                createPrivilegeIfNotFound("READ_PRIVILEGE"),
                createPrivilegeIfNotFound("WRITE_PRIVILEGE")
        )
        .thenMany(privilegeRepository.findAll())
        .collectList()
        .flatMap(privileges -> {
            Mono<Role> adminRole = createRoleIfNotFound("ROLE_ADMIN", privileges);
            Mono<Role> userRole = createRoleIfNotFound("ROLE_USER", privileges.stream()
                    .filter(privilege -> "READ_PRIVILEGE".equals(privilege.getName()))
                    .collect(Collectors.toList()));

            return Mono.when(adminRole, userRole);
        });
    }

    private Mono<Void> setupAdminUser() {
        return roleRepository.findByName("ROLE_ADMIN")
            .flatMap(adminRole -> userRepository.findByUsername("phucnguyen")
                .switchIfEmpty(
                    Mono.defer(() -> {
                        User user = new User();
                        user.setUsername("phucnguyen");
                        user.setEmail("phucnguyen@gmail.com");
                        user.setPassword(passwordEncoder.encode("SecureP@ssw0rd"));
                        user.setPhoneNumber("+84 35 539-0605");
                        user.setNationality(Nationality.USA);
                        user.setBirthDate(LocalDate.of(2002, 1,24));
                        user.setEnabled(true);
                        return userRepository.save(user);
                    })
                )
                .flatMap(user -> userRoleRepository.save(new UserRolePK(user.getId(), adminRole.getId())))
            ).then();
    }

    private Mono<Privilege> createPrivilegeIfNotFound(String name) {
        return privilegeRepository.findByName(name)
            .switchIfEmpty(
                privilegeRepository.save(new Privilege(name))
            );
    }

    private Mono<Role> createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        return roleRepository.findByName(name)
            .switchIfEmpty(
                roleRepository.save(new Role(name))
                    .flatMap(savedRole -> Flux.fromIterable(privileges)
                        .flatMap(privilege -> privilegeRepository.findByName(privilege.getName())
                            .flatMap(savedPrivilege -> rolePrivilegeRepository.save(new RolePrivilegePK(savedRole.getId(), savedPrivilege.getId())))
                        ).then(Mono.just(savedRole))
                    )
            );
    }
}

