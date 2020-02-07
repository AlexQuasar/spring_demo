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
        boolean registered = this.authenticationService.registration(dataMail);
        if (registered) {
            return "you are registered!";
        } else {
            return "you are NOT registered!";
        }
    }

    @GetMapping("/authorization")
    public String authorization(@RequestParam String login, @RequestParam String password) {
        boolean authorized = this.authenticationService.authorization(login, password);
        if (authorized) {
            return "Welcome";
        } else {
            return "Sorry pal. not this time";
        }
    }

    @GetMapping("/getPassword")
    public String getPassword(String token) {
        // TODO: 2/7/20 пароль наружу не предоставляют. Зачастую даже в базе хранят зашифрованным.
        //  Чтобы при получении пароля зашифровать его сразу и проверить совпадают ли шифры. Токен легко украсть,
        //  вся идея как раз в том чтобы предоставить по нему временный доступ. Если бы по токену можно было получить постоянный пароль,
        //  хакеры бы делали в первую очередь.
        return this.authenticationService.getPassword(token);
    }
}
