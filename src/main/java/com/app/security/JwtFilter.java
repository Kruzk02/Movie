package com.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class JwtFilter extends AuthenticationWebFilter {

    private final JwtUtil jwtUtil;
    private final ReactiveUserDetailsService reactiveUserDetailsService;

    @Autowired
    public JwtFilter(ReactiveAuthenticationManager authenticationManager, JwtUtil jwtUtil, ReactiveUserDetailsService reactiveUserDetailsService) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.reactiveUserDetailsService = reactiveUserDetailsService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = extractToken(exchange);
        if (token != null) {
            String username = jwtUtil.validateTokenAndGetUsername(token);
            String role = jwtUtil.validateTokenAndGetRole(token);
            Long id = jwtUtil.validateTokenAndGetId(token);
            if (username != null) {
                exchange.getAttributes().put("username",username);
                exchange.getAttributes().put("userId",id);
                exchange.getAttributes().put("role",role);
                return reactiveUserDetailsService.findByUsername(username)
                        .flatMap(userDetails -> {
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                        });
            }
            return chain.filter(exchange);
        }
        return chain.filter(exchange);
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
