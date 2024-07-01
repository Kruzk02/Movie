package com.app.Service;

import com.app.DTO.UserDTO;
import com.app.Entity.User;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> findById(Long id);
    Mono<User> getUserInfo(String token);
    Mono<User> update(Long id,UserDTO userDTO);
    Mono<String> login(UserDTO userDTO);
    Mono<User> register(UserDTO userDTO);
    Mono<Void> delete(Long id);
    Mono<Boolean> isUserHasAdminRole(String username);
}
