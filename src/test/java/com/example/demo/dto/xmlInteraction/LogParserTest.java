package com.example.demo.dto.xmlInteraction;

import com.example.demo.UserDataGenerator;
import com.example.demo.dto.xmlStructure.input.Log;
import com.example.demo.entity.User;
import com.example.demo.entity.UserVisit;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class LogParserTest {

    @Test
    public void parseTest() {
        int countDays = 3;
        int countUsers = 5;
        LocalDateTime date = LocalDateTime.now();
        UserDataGenerator userDataGenerator = new UserDataGenerator();
        List<Log> logs = userDataGenerator.generateLogs(1, countDays, countUsers, "site", date);
        Map<String, User> usersNameMap = userDataGenerator.getUsersNameMap();

        LogParser logParser = new LogParser(usersNameMap);
        List<UserVisit> visits = logParser.parse(logs);

        assertEquals(countDays * countUsers, visits.size());

        User user = visits.get(0).getUser();
        List<UserVisit> matches = visits.stream()
                .filter(i -> i.getUser().equals(user))
                .collect(Collectors.toList());
        assertEquals(countDays, matches.size());
    }
}