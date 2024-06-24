package com.app.Service;

import com.app.DTO.UserDTO;
import com.app.Entity.User;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> findById(Long id);
    Mono<User> findByUsername(String username);
    Mono<User> update(Long id,UserDTO userDTO);
    Mono<Void> login(UserDTO userDTO);
    Mono<User> register(UserDTO userDTO);
    Mono<Void> delete(Long id);
}
