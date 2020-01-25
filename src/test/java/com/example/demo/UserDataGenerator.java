package com.example.demo;

import com.example.demo.dto.xmlStructure.input.Log;
import com.example.demo.dto.xmlStructure.input.LogEntry;
import com.example.demo.entity.User;
import com.example.demo.entity.UserVisit;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.SneakyThrows;

import java.io.File;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class UserDataGenerator {

    private Random random = new Random();
    private XmlMapper xmlMapper = new XmlMapper();
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

    public UserVisit generateUserVisit(int id, LocalDateTime date, int countUsers, int timeSpent, String url) {
        if (countUsers == 0) {
            return null;
        }
        if (users.size() < countUsers) {
            generateUsers(countUsers - users.size());
        }

        UserVisit userVisit = new UserVisit();
        userVisit.setId(id);
        userVisit.setDay(date.toLocalDate());
        userVisit.setUser(users.get(random.nextInt(countUsers)));
        userVisit.setUrl(url);
        userVisit.setTimeSpent(timeSpent);
        userVisit.setTimeInterval(Duration.between(date, date.plusSeconds(timeSpent)));

        return userVisit;
    }

    public List<UserVisit> generateUserVisits(int countVisits, int countUsers, String url) {
        return generateUserVisits(countVisits, countUsers, 0, url);
    }

    public List<UserVisit> generateUserVisits(int countVisits, int countUsers, int timeSpent, String url) {
        List<UserVisit> visits = new ArrayList<>();

        if (countUsers == 0) {
            return visits;
        }

        generateUsers(countUsers);

        LocalDateTime date = LocalDateTime.now();
        for (int i = 1; i <= countVisits; i++) {
            if (timeSpent == 0) {
                timeSpent = 10 * i;
            }
            if (url.equals("")) {
                url = "urlSite" + i;
            }

            UserVisit userVisit = generateUserVisit(i, date, countUsers, timeSpent, url);
            visits.add(userVisit);
        }

        return visits;
    }

    public Log generateLog(int countDays, int countUsers, String url, LocalDateTime date) {
        return generateLog(countDays, countUsers, 0, url, 0L, date);
    }

    public Log generateLog(int countDays, int countUsers, int countUserDuplicate, String url, long seconds, LocalDateTime date) {
        Log log = new Log();
        List<LogEntry> journalEntries = new ArrayList<>();
        generateUsers(countUsers);

        for (int i = 1; i <= countDays; i++) {
            if (countUserDuplicate == 0) {
                countUserDuplicate = 1;
            }
            if (url.equals("")) {
                url = "urlSite" + i;
            }
            if (seconds == 0L) {
                seconds = 10L * i;
            }

            Long timestamp = Timestamp.valueOf(date.plusDays(i - 1)).getTime() / 1000L;
            for (User user : users) {
                for (int j = 0; j < countUserDuplicate; j++) {
                    LogEntry logEntry = new LogEntry(timestamp, user.getName(), url, seconds);
                    journalEntries.add(logEntry);
                }
            }
        }
        log.setLogEntries(journalEntries);

        return log;
    }

    public List<Log> generateLogs(int countLogs, int countDays, int countUsers, String url, LocalDateTime date) {
        return generateLogs(countLogs, countDays, countUsers, 0, url, 0L, date);
    }

    public List<Log> generateLogs(int countLogs, int countDays, int countUsers, int countUserDuplicate, String url, long seconds, LocalDateTime date) {
        List<Log> logs = new ArrayList<>();
        for (int i = 0; i < countLogs; i++) {
            Log log = generateLog(countDays, countUsers, countUserDuplicate, url, seconds, date);
            logs.add(log);
        }
        return logs;
    }

    @SneakyThrows
    public Log getLogFromFile(String filePath) {
        return xmlMapper.readValue(new File(filePath), Log.class);
    }

    public Map<String, User> getUsersNameMap() {
        Map<String, User> usersNameMap = new HashMap<>();
        for (User user : users) {
            usersNameMap.put(user.getName(), user);
        }
        return usersNameMap;
    }
}
