package com.example.demo.dto.tokenInteraction;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Getter
public class TokenGenerator {

    @Value("${authentication.delay}")
    private int delay;
    private final String key = "testKey";
    private Key securityKey;

    public String generateToken(String id) {
        byte[] keys = DatatypeConverter.parseBase64Binary(key);
        securityKey = new SecretKeySpec(keys, SignatureAlgorithm.HS512.getJcaName());
        Date date = new Date(Instant.now().plusSeconds(delay).toEpochMilli());
        return Jwts.builder().setId(id).setExpiration(date).signWith(SignatureAlgorithm.HS512, securityKey).compact();
    }
}
