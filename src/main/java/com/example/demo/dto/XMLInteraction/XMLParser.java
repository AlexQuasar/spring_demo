package com.example.demo.dto.XMLInteraction;

import com.example.demo.dto.structureXML.input.Input;
import com.example.demo.dto.structureXML.input.Log;
import com.example.demo.dto.support.UserIndicators;
import com.example.demo.dto.support.UserSite;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.Getter;

import java.time.*;
import java.util.HashMap;
import java.util.Map;

@Getter
public class XMLParser {

    private Map<LocalDate, Map<UserSite, UserIndicators>> visitsMap = new HashMap<>();
    private UserRepository userRepository;

    public XMLParser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void parseXML(Input input) {
        input.getLogs().forEach(this::writeUsersFromLog);
    }

    public void writeUsersFromLog(Log log) {
        long startTime = log.getTimestamp();
        long timeSpent = log.getSeconds();

        LocalDateTime startDateTime = LocalDateTime.ofEpochSecond(startTime, 0, ZoneOffset.UTC);
        LocalDateTime endOfDay = LocalDateTime.of(startDateTime.toLocalDate(), LocalTime.MAX);
        long secondsToEndDay = Duration.between(startDateTime, endOfDay).getSeconds();

        long timeSpentOnDay = timeSpent;
        while (timeSpentOnDay > 0) {
            timeSpentOnDay = timeSpent - secondsToEndDay;
            if (timeSpentOnDay <= 0) {
                addUserInMap(startDateTime.toLocalDate(), log.getUser_id(), log.getUrl(), timeSpent);
            } else {
                addUserInMap(startDateTime.toLocalDate(), log.getUser_id(), log.getUrl(), secondsToEndDay);
                timeSpent -= secondsToEndDay;

                startDateTime = startDateTime.plusDays(1L).withHour(0).withMinute(0).withSecond(0);
                endOfDay = endOfDay.plusDays(1L);
                secondsToEndDay = Duration.between(startDateTime, endOfDay).getSeconds();
            }
        }
    }

    private void addUserInMap(LocalDate date, int user_id, String url, long timeSpent) {
        User user = userRepository.findById(user_id);
        if (user == null) {
            user = new User();
            String userName = "Unknown" + user_id;
            user.setName(userName);
            userRepository.save(user);
        }

        UserSite userSite = new UserSite(user.getId(), date, user.getName(), url);

        Integer timeSpentInt = (int) timeSpent;
        Map<UserSite, UserIndicators> userFromMap = visitsMap.get(date);
        if (userFromMap != null) {
            UserIndicators userIndicators = userFromMap.get(userSite);
            if (userIndicators != null) {
                userIndicators.timeSpent += timeSpentInt;
            } else {
                userFromMap.put(userSite, new UserIndicators(timeSpentInt));
            }
        } else {
            Map<UserSite, UserIndicators> newSiteIndicators = new HashMap<>();
            newSiteIndicators.put(userSite, new UserIndicators(timeSpentInt));
            visitsMap.put(date, newSiteIndicators);
        }
    }
}
