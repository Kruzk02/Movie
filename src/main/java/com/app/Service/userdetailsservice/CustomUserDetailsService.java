package com.app.Service.userdetailsservice;

import com.app.Entity.Privilege;
import com.app.Entity.Role;
import com.app.Repository.RolePrivilegeRepository;
import com.app.Repository.UserRepository;
import com.app.Repository.UserRoleRepository;
import com.app.Service.userdetails.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePrivilegeRepository rolePrivilegeRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, UserRoleRepository userRoleRepository, RolePrivilegeRepository rolePrivilegeRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.rolePrivilegeRepository = rolePrivilegeRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
            .switchIfEmpty(Mono.error(new UsernameNotFoundException("Username not found")))
            .flatMap(user -> userRoleRepository.findRolesByUsername(username)
                .collectList()
                .flatMap(roles -> getAuthorities(roles)
                        .map(authorities -> new CustomUserDetails(
                                authorities,
                                user.getPassword(),
                                user.getUsername(),
                                true,
                                true,
                                true,
                                true
                        ))
                    )
                );
    }

    private Mono<Collection<? extends GrantedAuthority>> getAuthorities(Collection<Role> roles) {
        return getPrivileges(roles)
                .map(this::getGrantedAuthorities);
    }

    //don't ask because i can't name thing
    private Mono<List<String>> getPrivileges(Collection<Role> roles) {
        List<String> privileges = new ArrayList<>();

        List<Mono<List<Privilege>>> privilegeMono = roles.stream()
                .map(role -> rolePrivilegeRepository.findPrivilegeByRole(role.getName()).collectList())
                .collect(Collectors.toList());

        return Flux.merge(privilegeMono)
                .collectList()
                .map(privilegeLists -> {
                    privilegeLists.forEach(privilegeList ->
                            privilegeList.forEach(privilege ->
                                    privileges.add(privilege.getName())
                            )
                    );
                    return privileges;
                });
    }

    private Collection<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        return privileges.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
