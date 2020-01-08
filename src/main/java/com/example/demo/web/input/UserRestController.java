package com.example.demo.web.input;

import com.example.demo.dto.xmlStructure.input.Input;
import com.example.demo.entity.UserVisit;
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

    @PostMapping("/addVisits")
    public String addVisits(@RequestBody List<UserVisit> userVisits) {
        userParserService.addVisits(userVisits);
        return "all visits saved";
    }

    @PostMapping("/addVisit")
    public String addVisit(@RequestBody UserVisit userVisit) {
        userParserService.addVisit(userVisit);
        return "visit saved";
    }

    @GetMapping("/getGroupedUserVisits")
    public List<UserVisit> getGroupedUserVisits() {
        return userParserService.getGroupedUserVisits();
    }

    @PostMapping("/addLogs")
    // TODO: 1/8/20 пока лучше обойтись без xml, начнем с Json объекта с аналогичными полями
    public String addLogs(@RequestBody Input input) {
        userParserService.addLogs(input);
        return "logs added";
    }
}
