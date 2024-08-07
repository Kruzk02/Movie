package com.app.Config;

import com.app.Repository.RolePrivilegeRepository;
import com.app.Repository.RoleRepository;
import com.app.Repository.UserRepository;
import com.app.Repository.UserRoleRepository;
import com.app.Service.userdetailsservice.CustomUserDetailsService;
import com.app.jwt.JwtFilter;
import com.app.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.WebFilter;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePrivilegeRepository rolePrivilegeRepository;
    private final RateLimitFilter rateLimitFilter;

    @Autowired
    public SecurityConfig(JwtUtil jwtUtil, UserRepository userRepository, UserRoleRepository userRoleRepository, RolePrivilegeRepository rolePrivilegeRepository, RateLimitFilter rateLimitFilter) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.rolePrivilegeRepository = rolePrivilegeRepository;
        this.rateLimitFilter = rateLimitFilter;
    }

    @Bean
    SecurityWebFilterChain filterChain(ServerHttpSecurity https,ReactiveAuthenticationManager reactiveAuthenticationManager){
        return https
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authenticationManager(reactiveAuthenticationManager)
            .authorizeExchange(auth -> auth
                .anyExchange().permitAll()
            )
            .addFilterAt(rateLimitFilter,SecurityWebFiltersOrder.FIRST)
            .addFilterAt(jwtFilter(reactiveAuthenticationManager, reactiveUserDetailsService()), SecurityWebFiltersOrder.AUTHENTICATION)
            .build();
    }

    private WebFilter jwtFilter(ReactiveAuthenticationManager reactiveAuthenticationManager, ReactiveUserDetailsService reactiveUserDetailsService) {
        return new JwtFilter(reactiveAuthenticationManager, jwtUtil, reactiveUserDetailsService);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
       return new BCryptPasswordEncoder();
    }

    @Bean
    ReactiveUserDetailsService reactiveUserDetailsService(){
        return new CustomUserDetailsService(userRepository,userRoleRepository,rolePrivilegeRepository);
    }

    @Bean
    ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService reactiveUserDetailsService,PasswordEncoder passwordEncoder){
        UserDetailsRepositoryReactiveAuthenticationManager reactiveAuthenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(reactiveUserDetailsService);
        reactiveAuthenticationManager.setPasswordEncoder(passwordEncoder);
        return reactiveAuthenticationManager;
    }

    @Bean
    public RoleHierarchy roleHierarchy(){
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_STAFF\nROLE_STAFF > ROLE_USER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Bean
    public DefaultWebSecurityExpressionHandler customWebSecurityExpressionHandler(RoleHierarchy roleHierarchy){
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }
}
