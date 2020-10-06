package com.example.kubermarket;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;

@Slf4j
public class JwtUtil {

    private Key key;

    public JwtUtil(String secret){
        log.info(secret);
        this.key= Keys.hmacShaKeyFor(secret.getBytes());
        log.info(String.valueOf(key));
    }

    public String creatToken(String nickName){
        JwtBuilder builder= Jwts.builder()
                //.claim("userId",id)
                .claim("nickName",nickName);
        return builder.signWith(SignatureAlgorithm.HS256, key).compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }
}
