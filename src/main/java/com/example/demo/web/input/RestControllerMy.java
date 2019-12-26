package com.example.demo.web.input;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestControllerMy {


    @GetMapping
    public String ass(){
        return "Privetiki!";

    }

}
