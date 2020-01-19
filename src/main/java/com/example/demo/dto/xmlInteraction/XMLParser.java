package com.example.demo.dto.xmlInteraction;

import com.example.demo.dto.xmlStructure.input.Input;
import com.example.demo.dto.xmlStructure.input.Log;
import com.example.demo.dto.userInteraction.UserIndicators;
import com.example.demo.dto.userInteraction.UserSite;
import com.example.demo.entity.User;
import com.example.demo.entity.UserVisit;
import lombok.Getter;

import java.time.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Getter
public class XMLParser implements Runnable {

    private Map<LocalDate, Map<UserSite, UserIndicators>> visitsMap = new HashMap<>();
    private List<UserVisit> visits;
    private Map<String, User> usersNameMap;
    private Log log;

    public XMLParser(List<UserVisit> visits, Map<String, User> usersNameMap, Log log) {
        this.visits = visits;
        this.usersNameMap = usersNameMap;
        this.log = log;
    }

//    public void parseXML(Input input) {
//        input.getLogs().forEach(this::writeUsersFromLog);
//    }

    @Override
    public void run() {
        long startTime = log.getTimestamp();
        long timeSpent = log.getSeconds();

        LocalDateTime startDateTime = LocalDateTime.ofEpochSecond(startTime, 0, ZoneOffset.UTC);
        LocalDateTime endOfDay = LocalDateTime.of(startDateTime.toLocalDate(), LocalTime.MAX);
        long secondsToEndDay = Duration.between(startDateTime, endOfDay).getSeconds();

        String userName = log.getUserId();
        User user = usersNameMap.get(userName);
        if (user == null) {
            user = new User();
            user.setName(userName);
            usersNameMap.put(userName, user);
        }

        long timeSpentOnDay = timeSpent;
        while (timeSpentOnDay > 0) {
            timeSpentOnDay = timeSpent - secondsToEndDay;
            if (timeSpentOnDay <= 0) {
//                addUserInMap(user.getName(), startDateTime, log.getUrl(), timeSpent);
                addUserVisit(startDateTime, user, log.getUrl(), timeSpent);
            } else {
//                addUserInMap(user.getName(), startDateTime, log.getUrl(), secondsToEndDay);
                addUserVisit(startDateTime, user, log.getUrl(), secondsToEndDay);
                timeSpent -= secondsToEndDay;

                startDateTime = startDateTime.plusDays(1L).withHour(0).withMinute(0).withSecond(0);
                endOfDay = endOfDay.plusDays(1L);
                secondsToEndDay = Duration.between(startDateTime, endOfDay).getSeconds();
            }
        }
        // TODO: 1/19/20 со sleep зависает и тест не кончается
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    private void addUserVisit(LocalDateTime date, User user, String url, long timeSpent) {
        Integer timeSpentInt = (int) timeSpent;
        Duration timeInterval = Duration.between(date, date.plusSeconds(timeSpent));

        UserVisit userVisit = new UserVisit();
        userVisit.setDay(date.toLocalDate());
        userVisit.setUser(user);
        userVisit.setUrl(url);
        userVisit.setTimeSpent(timeSpentInt);
        userVisit.setTimeInterval(timeInterval);

        visits.add(userVisit);
    }

    private void addUserInMap(String userName, LocalDateTime date, String url, long timeSpent) {
        LocalDate day = date.toLocalDate();
        UserSite userSite = new UserSite(0, day, userName, url);

        Integer timeSpentInt = (int) timeSpent;
        Duration timeInterval = Duration.between(date, date.plusSeconds(timeSpent));
        Map<UserSite, UserIndicators> userFromMap = visitsMap.get(day);
        if (userFromMap != null) {
            UserIndicators userIndicators = userFromMap.get(userSite);
            if (userIndicators != null) {
                userIndicators.timeSpent += timeSpentInt;
            } else {
                userFromMap.put(userSite, new UserIndicators(timeSpentInt, timeInterval));
            }
        } else {
            Map<UserSite, UserIndicators> newSiteIndicators = new HashMap<>();
            newSiteIndicators.put(userSite, new UserIndicators(timeSpentInt, timeInterval));
            visitsMap.put(day, newSiteIndicators);
        }
    }
}
