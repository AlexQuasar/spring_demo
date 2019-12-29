package com.example.demo.services;

import com.example.demo.entity.User;
import com.example.demo.services.support.UserSite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {

    public List<User> sortUsers(List<User> users) {
        Map<UserSite, User> userSiteMap = new HashMap<>();

        for (User user : users) {
            UserSite userSite = new UserSite(user.getDay(), user.getUserId(), user.getUrl());
            User newUser = userSiteMap.get(userSite);
            if (newUser != null) {
                long average = (newUser.getAverage() + user.getAverage()) / 2L;
                newUser.setAverage(average);
            } else {
                userSiteMap.put(userSite, user);
            }
        }

        return new ArrayList<>(userSiteMap.values());
    }
}
