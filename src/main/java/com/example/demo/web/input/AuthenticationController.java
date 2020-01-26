package com.example.demo.web.input;

import com.example.demo.entity.Mail;
import com.example.demo.services.AuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
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
    // TODO: 1/26/20 обычно в качестве http сообщений испольхуют dto а не entity даже если они идентичны по пропертям.
    public String registration(@RequestBody Mail mail) {
        boolean registered = authenticationService.registration(mail);
        if (registered) {
            return "you are registered!";
        } else {
            return "you are NOT registered!";
        }
    }

    @GetMapping("/authorization")
    public String authorization(@RequestParam String login, @RequestParam String password) {
        //check presence in DB
        byte[] testKeys = DatatypeConverter.parseBase64Binary("testKey");
        Key key = new SecretKeySpec(testKeys, SignatureAlgorithm.HS512.getJcaName());
        String token = Jwts.builder().setId(login).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, key).compact();

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        Date expiration = body.getExpiration();
        if (expiration.after(new Date(Instant.now().toEpochMilli()))) {
            return "Welcome";
        } else {
            return "Sorry pal. not this time";
        }
    }
}
