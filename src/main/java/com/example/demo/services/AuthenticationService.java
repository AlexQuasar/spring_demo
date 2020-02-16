package com.example.demo.services;

import com.example.demo.dto.mailInteraction.DataMail;
import com.example.demo.dto.tokenInteraction.TokenGenerator;
import com.example.demo.entity.Mail;
import com.example.demo.entity.User;
import com.example.demo.entity.UserVisit;
import com.example.demo.exception.ServiceException;
import com.example.demo.repository.MailRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserVisitRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class AuthenticationService {

    private MailRepository mailRepository;
    private UserRepository userRepository;
    private UserVisitRepository userVisitRepository;
    private TokenGenerator tokenGenerator;

    public AuthenticationService(MailRepository mailRepository, UserRepository userRepository,
            UserVisitRepository userVisitRepository, TokenGenerator tokenGenerator) {
        this.mailRepository = mailRepository;
        this.userRepository = userRepository;
        this.userVisitRepository = userVisitRepository;
        this.tokenGenerator = tokenGenerator;
    }

    public Boolean registration(DataMail dataMail) {
        Mail mail = this.mailRepository.findByLogin(dataMail.getLogin());
        if (mail == null) {
            User user = this.userRepository.findByName(dataMail.getUserName());
            if (user != null) {
                this.mailRepository.save(new Mail(dataMail, user));
                return true;
            }
        }

        return false;
    }

    public Boolean authorization(String login, String password) {
        if (isMailExist(login, password)) {
            String token = this.tokenGenerator.generateToken(login);

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(this.tokenGenerator.getSecurityKey()).parseClaimsJws(token);
            Claims body = claimsJws.getBody();

            Date expiration = body.getExpiration();
            return expiration.after(new Date(Instant.now().toEpochMilli()));
        }

        return false;
    }

    private Boolean isMailExist(String login, String password) {
        Mail mail = this.mailRepository.findByLogin(login);
        return mail != null && mail.getPassword().equals(password);
    }

    public List<UserVisit> getTodayVisits(String token) throws ServiceException {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(this.tokenGenerator.getSecurityKey()).parseClaimsJws(token);
        Claims body = claimsJws.getBody();

        Mail mail = this.mailRepository.findByLogin(body.getId());
        if (mail == null) {
            throw new ServiceException("You not registered", HttpStatus.FORBIDDEN);
        }

        Date expiration = body.getExpiration();
        if (expiration.after(new Date(Instant.now().toEpochMilli()))) {
            User user = userRepository.findById(mail.getUser().getId());
            return userVisitRepository.findAllByUserAndDay(user, LocalDate.now());
        } else {
            throw new ServiceException("Please, authorized again", HttpStatus.UNAUTHORIZED);
        }
    }
}
