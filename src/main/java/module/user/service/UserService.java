package module.user.service;

import module.user.dto.UserDTO;
import module.user.entity.User;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> findById(Long id);
    Mono<User> update(Long id,UserDTO userDTO);
    Mono<String> login(UserDTO userDTO);
    Mono<User> register(UserDTO userDTO);
    Mono<Void> delete(Long id);
}
