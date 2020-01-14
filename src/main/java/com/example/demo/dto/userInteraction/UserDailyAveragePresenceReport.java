package com.example.demo.dto.userInteraction;

import com.example.demo.entity.User;
import com.example.demo.entity.UserVisit;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class UserDailyAveragePresenceReport {

    Map<String, User> usersNameMap;
    List<UserVisit> userVisits;

    public List<UserAveragePresence> getGroupUsers() {
        Map<UserSite, UserIndicators> userSiteMap = new HashMap<>();

        for (UserVisit userVisit : userVisits) {
            UserSite userSite = new UserSite(userVisit.getUser().getId(), userVisit.getDay(), userVisit.getUser().getName(), userVisit.getUrl());
            UserIndicators userIndicators = userSiteMap.get(userSite);
            if (userIndicators != null) {
                userIndicators.timeSpent += userVisit.getTimeSpent();
                userIndicators.timeInterval = userIndicators.timeInterval.plusSeconds(userVisit.getTimeInterval().getSeconds());
                userIndicators.countAdditions++;
            } else {
                userSiteMap.put(userSite, new UserIndicators(userVisit.getId(), userVisit.getTimeSpent(), userVisit.getTimeInterval()));
            }
        }

        List<UserAveragePresence> userAveragePresences = new ArrayList<>();
        for (Map.Entry<UserSite, UserIndicators> entry : userSiteMap.entrySet()) {
            UserSite userSite = entry.getKey();
            UserIndicators userIndicators = entry.getValue();
            UserVisit userVisit = new UserVisit();
            userVisit.setId(userIndicators.user_visit_id);
            userVisit.setDay(userSite.day);
            userVisit.setUser(usersNameMap.get(userSite.userName));
            userVisit.setUrl(userSite.url);
            userVisit.setTimeSpent(userIndicators.timeSpent);
            userVisit.setTimeInterval(userIndicators.timeInterval);
            int average = userIndicators.timeSpent / userIndicators.countAdditions;
            UserAveragePresence userAveragePresence = new UserAveragePresence(userVisit, average);
            userAveragePresences.add(userAveragePresence);
        }

        return userAveragePresences;
    }
}
