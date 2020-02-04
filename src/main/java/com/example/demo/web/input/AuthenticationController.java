package com.example.demo.web.input;

import com.example.demo.dto.mailInteraction.DataMail;
import com.example.demo.services.AuthenticationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
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
        boolean authorized = authenticationService.authorization(login, password);
        if (authorized) {
            return "Welcome";
        } else {
            return "Sorry pal. not this time";
        }
    }

    @GetMapping("/getPassword")
    public String getPassword(String token) {
        return authenticationService.getPassword(token);
    }
}
