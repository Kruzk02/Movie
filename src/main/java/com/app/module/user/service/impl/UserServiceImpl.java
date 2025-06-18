package com.app.module.user.service.impl;

import com.app.module.user.dto.UserDTO;
import com.app.module.user.entity.User;
import com.app.module.user.entity.UserRolePK;
import com.app.Expection.*;
import com.app.module.user.mapper.UserMapper;
import com.app.module.user.repository.RoleRepository;
import com.app.module.user.repository.UserRepository;
import com.app.module.user.repository.UserRoleRepository;
import com.app.module.user.service.UserService;
import com.app.security.JwtUtil;
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

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReactiveAuthenticationManager reactiveAuthenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder, ReactiveAuthenticationManager reactiveAuthenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.reactiveAuthenticationManager = reactiveAuthenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<User> findById(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFound("User not found with a id: " + id)))
                .log("Find user with a id: " + id);
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
    public Mono<String> login(UserDTO userDTO) {
        return reactiveAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        userDTO.getUsername(), userDTO.getPassword()
                ))
                .doOnNext(ReactiveSecurityContextHolder::withAuthentication)
                .flatMap(authentication -> userRepository.findByUsername(userDTO.getUsername())
                    .flatMap(user -> userRoleRepository.findRoleByUsername(user.getUsername())
                            .map(role -> jwtUtil.generateToken(user.getUsername(),user.getId(), role.getName()))))
                .onErrorResume(ex -> Mono.error(new UserNotExistingException("User not existing " + ex.getMessage())))
                .doOnNext(token -> System.out.println("Login with a username: " + userDTO.getUsername() + ", token: " + token));
    }

    @Override
    public Mono<User> register(UserDTO userDTO) {
        return checkUsernameExist(userDTO.getUsername())
                .then(checkEmailExist(userDTO.getEmail()))
                .then(Mono.defer(() -> {
                    User user = UserMapper.INSTANCE.mapDTOToEntity(userDTO);
                    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                    user.setEnabled(false);

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
                }));
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

    private Mono<Void> checkUsernameExist(String username){
        return userRepository.findByUsername(username)
                .flatMap(ExistingUser -> Mono.error(new UsernameAlreadyExistsException("Username already exists: " + username)))
                .switchIfEmpty(Mono.empty()).then();
    }

    private Mono<Void> checkEmailExist(String email){
        return userRepository.findByEmail(email)
                .flatMap(existingUser -> Mono.error(new EmailAlreadyExistingException("Email already exist: " + email)))
                .switchIfEmpty(Mono.empty()).then();
    }
}
