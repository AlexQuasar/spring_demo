package com.example.demo.services;

import com.example.demo.dto.mailInteraction.DataMail;
import com.example.demo.dto.mailInteraction.SessionLife;
import com.example.demo.entity.Mail;
import com.example.demo.repository.MailRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    @Value("${authentication.delay}")
    private int delay;
    private Date expirationDate;
    private MailRepository mailRepository;
    private Map<String, SessionLife> tokenMap = new HashMap<>();

    public AuthenticationService(MailRepository mailRepository) {
        this.mailRepository = mailRepository;
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
        Mail mail = mailRepository.findByLogin(login);
        if (mail != null && mail.getPassword().equals(password)) {
            byte[] testKeys = DatatypeConverter.parseBase64Binary("testKey");
            Key key = new SecretKeySpec(testKeys, SignatureAlgorithm.HS512.getJcaName());
            expirationDate = new Date(Instant.now().plusSeconds(delay).toEpochMilli());
            String token = Jwts.builder().setId(login).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, key).compact();
            tokenMap.put(token, new SessionLife(mail, new Date(Instant.now().toEpochMilli())));

            // TODO: 03.02.2020 как юзать Jws я до конца не понял, сделал через мапу
            // TODO: 2/4/20  4 строки ниже показыввают как. Мапа тебе не нужна.
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

    public String getPassword(String token) {
        // TODO: 2/4/20 инфа о просрочке хранится в самом токне тебе нужно просто проверить не протух ли он. Сессию заводить не стоит.
        // а как найти Mail по токену?
        SessionLife sessionLife = tokenMap.get(token);
        if (sessionLife == null) {
            return "You not authorized";
        }

        byte[] testKeys = DatatypeConverter.parseBase64Binary("testKey");
        Key key = new SecretKeySpec(testKeys, SignatureAlgorithm.HS512.getJcaName());

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        Date expiration = body.getExpiration();

        if (expiration.after(new Date(Instant.now().toEpochMilli()))) {
            return sessionLife.getMail().getPassword();
        } else {
            return "Please, authorized again";
        }
    }
}
