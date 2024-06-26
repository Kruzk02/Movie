package com.app.jwt;

import com.app.Service.userdetailsservice.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class JwtFilter extends AuthenticationWebFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public JwtFilter(ReactiveAuthenticationManager authenticationManager, JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return extractToken(exchange)
            .flatMap(token -> {
                String username = jwtUtil.validateTokenAndGetUsername(token);
                if (username != null) {
                    return customUserDetailsService.findByUsername(username)
                        .flatMap(userDetails -> {
                            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            return ReactiveSecurityContextHolder.getContext()
                                .map(securityContext -> {
                                    securityContext.setAuthentication(authentication);
                                    return securityContext;
                                })
                                .then(chain.filter(exchange));
                        });
                }
                return chain.filter(exchange);
            })
            .switchIfEmpty(chain.filter(exchange));
    }

    private Mono<String> extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return Mono.just(authHeader.substring(7));
        }
        return Mono.empty();
    }
}
