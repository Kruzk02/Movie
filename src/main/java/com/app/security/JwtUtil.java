package com.app.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final RSAPublicKey rPubKey;
    private final RSAPrivateKey rPriKey;

    public JwtUtil() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048 );
        KeyPair kp = kpg.generateKeyPair();
        rPubKey = (RSAPublicKey) kp.getPublic();
        rPriKey = (RSAPrivateKey) kp.getPrivate();
    }

    public String generateToken(String username){
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // Token expires in 10 minutes
                .sign(Algorithm.RSA512(rPubKey,rPriKey));
    }

    public String generateToken(String username,Long id){
        return JWT.create()
                .withClaim("userId",id)
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .sign(Algorithm.RSA512(rPubKey,rPriKey));
    }

    public String generateToken(String username, Long id,String role){
        return JWT.create()
                .withClaim("userId",id)
                .withClaim("role",role)
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .sign(Algorithm.RSA512(rPubKey,rPriKey));
    }

    public DecodedJWT validateToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.RSA512(rPubKey, rPriKey)).build();
        return verifier.verify(token);
    }

    public String validateTokenAndGetUsername(String token) throws JWTVerificationException {
        DecodedJWT decodedJWT = validateToken(token);
        return decodedJWT.getSubject();
    }

    public Long validateTokenAndGetId(String token) throws JWTVerificationException{
        DecodedJWT decodedJWT = validateToken(token);
        return decodedJWT.getClaim("userId").asLong();
    }

    public String validateTokenAndGetRole(String token) {
        DecodedJWT decodedJWT = validateToken(token);
        return decodedJWT.getClaim("role").asString();
    }
}

