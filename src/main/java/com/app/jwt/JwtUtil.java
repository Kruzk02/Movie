package com.app.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
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
        kpg.initialize(1024);
        KeyPair kp = kpg.generateKeyPair();
        rPubKey = (RSAPublicKey) kp.getPublic();
        rPriKey = (RSAPrivateKey) kp.getPrivate();
    }

    public String generateToken(String username){
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // Token expires in 10 minutes
                .sign(Algorithm.RSA256(rPubKey,rPriKey));
    }

    public String validateTokenAndGetUsername(String token){
        JWTVerifier verifier = JWT.require(Algorithm.RSA256(rPubKey,rPriKey)).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject();
    }
}