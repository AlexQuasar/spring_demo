package com.example.demo.services;

import com.example.demo.dto.mailInteraction.DataMail;
import com.example.demo.dto.tokenInteraction.TokenGenerator;
import com.example.demo.entity.Mail;
import com.example.demo.repository.MailRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class AuthenticationService {

    private MailRepository mailRepository;
    private TokenGenerator tokenGenerator = new TokenGenerator();

    public AuthenticationService(MailRepository mailRepository) {
        this.mailRepository = mailRepository;
    }

    public Boolean registration(DataMail dataMail) {
        Mail mail = this.mailRepository.findByLogin(dataMail.getLogin());
        if (mail == null) {
            this.mailRepository.save(new Mail(dataMail));
            return true;
        }

        return false;
    }

    public Boolean authorization(String login, String password) {
        if (isMailExist(login, password)) {
            String token = tokenGenerator.generateToken(login);

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenGenerator.getSecurityKey()).parseClaimsJws(token);
            Claims body = claimsJws.getBody();

            // TODO: 2/9/20 перенес в отдельный класс генерацию токена и теперь ключ всегда протухший (пока не разобрался в чем проблема)
            Date expiration = body.getExpiration();
            return expiration.after(new Date(Instant.now().toEpochMilli()));
        }

        return false;
    }

    private Boolean isMailExist(String login, String password) {
        Mail mail = this.mailRepository.findByLogin(login);
        return mail != null && mail.getPassword().equals(password);
    }

    public String getPassword(String token) {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenGenerator.getSecurityKey()).parseClaimsJws(token);
        Claims body = claimsJws.getBody();

        Mail mail = mailRepository.findByLogin(body.getId());
        if (mail == null) {
            return "You not authorized";
        }

        Date expiration = body.getExpiration();
        if (expiration.after(new Date(Instant.now().toEpochMilli()))) {
            return mail.getPassword();
        } else {
            return "Please, authorized again";
        }
    }
}
