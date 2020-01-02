package com.example.demo.web.input;

import com.example.demo.entity.User;
import com.example.demo.services.UserParserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userController")
public class UserRestController {

    UserParserService userParserService;

    public UserRestController(UserParserService userParserService) {
        this.userParserService = userParserService;
    }

    @PostMapping("/addUsers")
    public String addUsers(@RequestBody List<User> users) {
        userParserService.addUsers(users);
        return "all users saved";
    }

    @PostMapping("/addUser")
    public String addUser(@RequestBody User user) {
        // TODO: проблема с id, так как он сам инкрементируется. не могу разобраться
        userParserService.addUser(user);
        return "user saved";
    }

    @GetMapping("/getSortedUsers")
    public List<User> getSortedUsers() {
        return userParserService.getSortedUsers();
    }
}
