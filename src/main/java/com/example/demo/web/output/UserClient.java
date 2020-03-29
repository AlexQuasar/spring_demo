package com.example.demo.web.output;

import com.example.demo.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "authenticationService", url = "http://localhost:8090/userController")
public interface UserClient {

    @RequestMapping(method = RequestMethod.POST, value = "/addUser")
    String addUser(@RequestBody User user);

    @RequestMapping(method = RequestMethod.POST, value = "/addUsers")
    String addUsers(@RequestBody List<User> users);

    @RequestMapping(method = RequestMethod.GET, value = "/findById")
    User findById(@RequestParam int id);

    @RequestMapping(method = RequestMethod.GET, value = "/findByName")
    User findByName(@RequestParam String name);

    @RequestMapping(method = RequestMethod.GET, value = "/findAll")
    List<User> findAll();
}
