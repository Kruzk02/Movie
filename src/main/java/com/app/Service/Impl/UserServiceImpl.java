package com.app.Service.Impl;

import com.app.DTO.UserDTO;
import com.app.Entity.User;
import com.app.Entity.UserRolePK;
import com.app.Expection.*;
import com.app.Mapper.UserMapper;
import com.app.Repository.RoleRepository;
import com.app.Repository.UserRepository;
import com.app.Repository.UserRoleRepository;
import com.app.Service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LogManager.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReactiveAuthenticationManager reactiveAuthenticationManager;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder, ReactiveAuthenticationManager reactiveAuthenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.reactiveAuthenticationManager = reactiveAuthenticationManager;
    }

    @Override
    public Mono<User> findById(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFound("User not found with a id: " + id)))
                .log("Find user with a id: " + id);
    }

    @Override
    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new UserNotFound("User not found with a username: " + username)))
                .log("Find user with a username: " + username);
    }

    @Override
    public Mono<User> update(Long id, UserDTO userDTO) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFound("User not found with a id: " + id)))
                .flatMap(existingUser -> {
                    existingUser.setUsername(userDTO.getUsername());
                    existingUser.setEmail(userDTO.getEmail());
                    existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                    existingUser.setNationality(userDTO.getNationality());
                    existingUser.setPhoneNumber(userDTO.getPhoneNumber());
                    existingUser.setBirthDate(userDTO.getBirthDate());

                    return userRepository.save(existingUser);
                })
                .log("Update user with a id: " + id);
    }

    @Override
    public Mono<Void> login(UserDTO userDTO) {
        return reactiveAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userDTO.getUsername(), userDTO.getPassword()
        ))
        .doOnSuccess(ReactiveSecurityContextHolder::withAuthentication)
        .onErrorResume(Exception.class, ex -> Mono.error(new Exception("Authentication failed", ex)))
        .then()
        .log("Login with a username: " + userDTO.getUsername());
    }

    @Override
    public Mono<User> register(UserDTO userDTO) {
        User user = UserMapper.INSTANCE.mapDTOToEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        if (!validateEmail(user.getEmail())){
            throw new EmailValidationException("Invalid email address format: " + user.getEmail());
        }

        if (!validatePassword(user.getPassword())){
            throw new PasswordValidationException();
        }

        if (!validatePhoneNumber(user.getPhoneNumber())){
            throw new PhoneNumberValidationException("Invalid phone number format: " + user.getPhoneNumber());
        }

        return userRepository.save(user)
                .flatMap(savedUser -> roleRepository.findByName("ROLE_USER")
                        .flatMap(roleUser -> userRoleRepository.save(new UserRolePK(savedUser.getId(),roleUser.getId())))
                        .thenReturn(savedUser))
                .log("Register with a email: " + user.getEmail());
    }

    @Override
    public Mono<Void> delete(Long id) {
        return userRepository.findById(id)
            .switchIfEmpty(Mono.error(new UserNotFound("User not found with a id: " + id)))
            .flatMap(userRepository::delete)
            .log("Delete user with a id: " + id);
    }

    //thienphuc123456@gmail.com
    private boolean validateEmail(String email){
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //SecureP@ssw0rd
    private boolean validatePassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    //+84 35 539-0605
    private boolean validatePhoneNumber(String phoneNumber){
        Pattern pattern = Pattern.compile("^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
