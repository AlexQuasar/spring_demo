package com.example.demo;

import com.example.demo.dto.xmlStructure.input.Input;
import com.example.demo.dto.xmlStructure.input.Log;
import com.example.demo.entity.User;
import com.example.demo.entity.UserVisit;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class UserDataGenerator {

    private Random random = new Random();
    public List<User> users = new ArrayList<>();

    public List<User> generateUsers(int count) {
        for (int i = 1; i <= count; i++) {
            User user = new User();
            user.setId(i);
            user.setName("userName" + i);
            users.add(user);
        }
        return users;
    }

    public List<UserVisit> generateUserVisits(int countVisits, int countUsers, String url) {
        return generateUserVisits(countVisits, countUsers, 0, url);
    }

    public List<UserVisit> generateUserVisits(int countVisits, int countUsers, int timeSpent, String url) {
        List<UserVisit> visits = new ArrayList<>();
        generateUsers(countUsers);

        LocalDateTime date = LocalDateTime.now();
        for (int i = 1; i <= countVisits; i++) {
            if (timeSpent == 0) {
                timeSpent = 10 * i;
            }
            if (url.equals("")) {
                url = "urlSite" + i;
            }

            UserVisit userVisit = new UserVisit();
            userVisit.setId(i);
            userVisit.setDay(date.toLocalDate());
            userVisit.setUser(users.get(random.nextInt(countUsers)));
            userVisit.setUrl(url);
            userVisit.setTimeSpent(timeSpent);
            userVisit.setTimeInterval(Duration.between(date, date.plusSeconds(timeSpent)));
            visits.add(userVisit);
        }

        return visits;
    }

    public Input generateInput(int countDays, int countUsers, String url, LocalDateTime date) {
        return generateInput(countDays, countUsers, url, 0L, date);
    }

    public Input generateInput(int countDays, int countUsers, String url, long seconds, LocalDateTime date) {
        Input input = new Input();
        List<Log> logs = new ArrayList<>();
        generateUsers(countUsers);

        for (int i = 1; i <= countDays; i++) {
            if (url.equals("")) {
                url = "urlSite" + i;
            }
            if (seconds == 0L) {
                seconds = 10L * i;
            }

            Long timestamp = Timestamp.valueOf(date.plusDays(i - 1)).getTime() / 1000L;
            for (User user : users) {
                Log log = new Log(timestamp, user.getName(), url, seconds);
                logs.add(log);
            }
        }
        input.setLogs(logs);

        return input;
    }

    public Map<String, User> getUsersNameMap() {
        Map<String, User> usersNameMap = new HashMap<>();
        for (User user : users) {
            usersNameMap.put(user.getName(), user);
        }
        return usersNameMap;
    }
}
