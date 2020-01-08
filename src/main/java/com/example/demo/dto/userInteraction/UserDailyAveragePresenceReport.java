package com.example.demo.dto.userInteraction;

import com.example.demo.entity.UserVisit;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class UserDailyAveragePresenceReport {

    UserRepository userRepository;
    List<UserVisit> userVisits;

    public List<UserVisit> getGroupUsers() {
        Map<UserSite, UserIndicators> userSiteMap = new HashMap<>();

        for (UserVisit userVisit : userVisits) {
            UserSite userSite = new UserSite(userVisit.getUser().getId(), userVisit.getDay(), userVisit.getUser().getName(), userVisit.getUrl());
            UserIndicators userIndicators = userSiteMap.get(userSite);
            if (userIndicators != null) {
                userIndicators.timeSpent += userVisit.getTimeSpent();
            } else {
                userSiteMap.put(userSite, new UserIndicators(userVisit.getId(), userVisit.getTimeSpent(), userVisit.getTimeInterval()));
            }
        }

        List<UserVisit> userVisitList = new ArrayList<>();
        for (Map.Entry<UserSite, UserIndicators> entry : userSiteMap.entrySet()) {
            UserSite userSite = entry.getKey();
            UserIndicators userIndicators = entry.getValue();
            UserVisit userVisit = new UserVisit();
            userVisit.setId(userIndicators.user_visit_id);
            userVisit.setDay(userSite.day);
            userVisit.setUser(userRepository.findById(userSite.user_id));
            userVisit.setUrl(userSite.url);
            userVisit.setTimeSpent(userIndicators.timeSpent);
            userVisit.setTimeInterval(userIndicators.timeInterval);
            userVisitList.add(userVisit);
        }

        return userVisitList;
    }
}
