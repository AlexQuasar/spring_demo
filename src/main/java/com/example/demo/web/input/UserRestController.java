package com.example.demo.web.input;

import com.example.demo.dto.userInteraction.UserAveragePresence;
import com.example.demo.dto.xmlStructure.input.Log;
import com.example.demo.entity.UserVisit;
import com.example.demo.services.UserParserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userController")
@Validated
public class UserRestController {

    private UserParserService userParserService;

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
    public List<UserAveragePresence> getGroupedUserVisits() {
        return userParserService.getGroupedUserVisits();
    }

    @PostMapping("/addLog")
    public String addLog(@RequestBody List<Log> logs) {
        userParserService.addLog(logs);
        return "logs added";
    }
}
