package com.example.demo.web.input;

import com.example.demo.entity.UserTest;
import com.example.demo.services.SimpleService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/controller1")
public class RestControllerMy {

    SimpleService simpleService;

    public RestControllerMy(SimpleService simpleService) {
        this.simpleService = simpleService;
    }

    @GetMapping
    public String ass() {
        return "Privetiki!";
    }

    @GetMapping("/user")
    public List<UserTest> ass1(@RequestParam String name) {
        return simpleService.getAllUsersByName(name);
    }

    @GetMapping("/user/{id}")
    public List<UserTest> assWithParam(@PathVariable int id) {
        UserTest user = new UserTest();
        user.setId(id);
        user.setName("Petya");
        ArrayList<UserTest> list = new ArrayList<>();
        list.add(user);
        return list;
    }

    @PostMapping("/user")
    public void addUser(@RequestBody UserTest user){
        System.out.println(user);
        simpleService.saveUser(user);
//        return "User added";
    }
}
