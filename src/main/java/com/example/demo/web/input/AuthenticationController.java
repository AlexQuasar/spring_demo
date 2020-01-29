package com.example.demo.web.input;

import com.example.demo.dto.mailInteraction.DataMail;
import com.example.demo.services.AuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.omg.IOP.CodecOperations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jta.atomikos.AtomikosDependsOnBeanFactoryPostProcessor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    // TODO: 1/29/20 тогда уже проще в сервис перенести
    @Value("${authentication.delay}")
    private int delay;

    private Date expirationDate;
    private AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostConstruct
    private void init() {
        expirationDate = new Date(Instant.now().plusSeconds(delay).toEpochMilli());
    }

    @PostMapping("/registration")
    public String registration(@RequestBody DataMail dataMail) {
        boolean registered = authenticationService.registration(dataMail);
        if (registered) {
            return "you are registered!";
        } else {
            return "you are NOT registered!";
        }
    }

    @GetMapping("/authorization")
    public String authorization(@RequestParam String login, @RequestParam String password) {
        boolean authorized = authenticationService.authorization(login, password, expirationDate);
        if (authorized) {
            return "Welcome";
        } else {
            return "Sorry pal. not this time";
        }
    }
}
