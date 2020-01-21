package com.example.demo.dto.xmlInteraction;

import com.example.demo.dto.xmlStructure.input.Log;
import com.example.demo.entity.User;
import com.example.demo.entity.UserVisit;

import java.time.*;
import java.util.List;
import java.util.Map;

public class XMLParser implements Runnable {

    private List<UserVisit> visits;
    private Map<String, User> usersNameMap;
    private Log log;

    public XMLParser(List<UserVisit> visits, Map<String, User> usersNameMap, Log log) {
        this.visits = visits;
        this.usersNameMap = usersNameMap;
        this.log = log;
    }

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
                addUserVisit(startDateTime, user, log.getUrl(), timeSpent);
            } else {
                addUserVisit(startDateTime, user, log.getUrl(), secondsToEndDay);
                timeSpent -= secondsToEndDay;

                startDateTime = startDateTime.plusDays(1L).withHour(0).withMinute(0).withSecond(0);
                endOfDay = endOfDay.plusDays(1L);
                secondsToEndDay = Duration.between(startDateTime, endOfDay).getSeconds();
            }
        }
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
}
