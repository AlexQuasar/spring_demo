package com.example.demo.services;

import com.example.demo.dto.mailInteraction.DataMail;
import com.example.demo.entity.Mail;
import com.example.demo.repository.MailRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Service
public class AuthenticationService {

    @Value("${authentication.delay}")
    private int delay;
    private Date expirationDate;
    private MailRepository mailRepository;

    public AuthenticationService(MailRepository mailRepository) {
        this.mailRepository = mailRepository;
    }

    @PostConstruct
    private void init() {
        expirationDate = new Date(Instant.now().plusSeconds(delay).toEpochMilli());
    }

    public Boolean registration(DataMail dataMail) {
        Mail mail = mailRepository.findByLogin(dataMail.getLogin());
        if (mail == null) {
            mailRepository.save(new Mail(dataMail));
            return true;
        }

        return false;
    }

    public Boolean authorization(String login, String password) {
        if (isMailExist(login, password)) {
            byte[] testKeys = DatatypeConverter.parseBase64Binary("testKey");
            Key key = new SecretKeySpec(testKeys, SignatureAlgorithm.HS512.getJcaName());
            String token = Jwts.builder().setId(login).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, key).compact();
            // TODO: 28.01.2020 как зафиксировать этот токен, чтобы можно было к нему обратиться, и сверить, уже в другом методе, когда пользак будет делать запрос?

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            Claims body = claimsJws.getBody();
            Date expiration = body.getExpiration();
            return expiration.after(new Date(Instant.now().toEpochMilli()));
        }

        return false;
    }

    private Boolean isMailExist(String login, String password) {
        Mail mail = mailRepository.findByLogin(login);
        return mail != null && mail.getPassword().equals(password);
    }
}
