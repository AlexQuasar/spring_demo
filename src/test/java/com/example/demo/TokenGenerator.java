package com.example.demo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Getter
public class TokenGenerator {

    private long delay;
    private final String key = "testKey";
    private Key securityKey;

    public TokenGenerator(long delay) {
        this.delay = delay;
        byte[] keys = DatatypeConverter.parseBase64Binary(key);
        securityKey = new SecretKeySpec(keys, SignatureAlgorithm.HS512.getJcaName());
    }

    public String generateToken(String id) {
        Date date = new Date(Instant.now().plusSeconds(delay).toEpochMilli());
        return Jwts.builder().setId(id).setExpiration(date).signWith(SignatureAlgorithm.HS512, securityKey).compact();
    }

    public String getExistToken(String id) {
        return Jwts.builder().setId(id).signWith(SignatureAlgorithm.HS512, securityKey).compact();
    }
}
