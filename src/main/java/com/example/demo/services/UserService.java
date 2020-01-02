package com.example.demo.services;

import com.example.demo.entity.User;
import com.example.demo.entity.support.UserIndicators;
import com.example.demo.entity.support.UserSite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {

    public List<User> sortUsers(List<User> users) {
        Map<UserSite, UserIndicators> userSiteMap = new HashMap<>();

        for (User user : users) {
            UserSite userSite = new UserSite(user.getDay(), user.getUserId(), user.getUrl());
            UserIndicators userIndicators = userSiteMap.get(userSite);
            if (userIndicators != null) {
                userIndicators.timeSpent += user.getAverage();
                userIndicators.visitQuantity++;
            } else {
                userSiteMap.put(userSite, new UserIndicators(user.getAverage()));
            }
        }

        List<User> userList = new ArrayList<>();
        for (Map.Entry<UserSite, UserIndicators> entry : userSiteMap.entrySet()) {
            UserSite userSite = entry.getKey();
            UserIndicators userIndicators = entry.getValue();
            User user = new User();
            user.setDay(userSite.day);
            user.setUserId(userSite.userId);
            user.setUrl(userSite.url);
            user.setAverage(userIndicators.timeSpent / userIndicators.visitQuantity);
            userList.add(user);
        }

        return userList;
    }
}
