package com.example.demo.web.output;

import com.example.demo.dto.mailInteraction.DataMail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "authenticationService", url = "http://localhost:8080/authentication")
public interface AuthenticationClient {

    @RequestMapping(method = RequestMethod.POST, value = "/registration")
    String registration(@RequestBody DataMail dataMail);
}
