package com.example.demo.services;

import com.example.demo.dto.support.UserIndicators;
import com.example.demo.dto.support.UserSite;
import com.example.demo.entity.UserVisit;
import com.example.demo.repository.UserVisitRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserParserService {

    private UserVisitRepository userVisitRepository;
    private UserRepository userRepository;

    public UserParserService(UserVisitRepository userVisitRepository, UserRepository userRepository) {
        this.userVisitRepository = userVisitRepository;
        this.userRepository = userRepository;
    }

    public void addVisit(UserVisit userVisit) {
        userVisitRepository.save(userVisit);
    }

    public void addVisits(List<UserVisit> userVisits) {
        userVisitRepository.saveAll(userVisits);
    }

    public List<UserVisit> getGroupedUserVisits() {
        return getGroupUsers(userVisitRepository.findAll());
    }

    private List<UserVisit> getGroupUsers(List<UserVisit> userVisits) {
        Map<UserSite, UserIndicators> userSiteMap = new HashMap<>();

        for (UserVisit userVisit : userVisits) {
            UserSite userSite = new UserSite(userVisit.getUser().getId(), userVisit.getDay(), userVisit.getUser().getName(), userVisit.getUrl());
            UserIndicators userIndicators = userSiteMap.get(userSite);
            if (userIndicators != null) {
                userIndicators.timeSpent += userVisit.getAverage();
                userIndicators.visitQuantity++;
            } else {
                userSiteMap.put(userSite, new UserIndicators(userVisit.getAverage()));
            }
        }

        List<UserVisit> userVisitList = new ArrayList<>();
        for (Map.Entry<UserSite, UserIndicators> entry : userSiteMap.entrySet()) {
            UserSite userSite = entry.getKey();
            UserIndicators userIndicators = entry.getValue();
            UserVisit userVisit = new UserVisit();
            userVisit.setDay(userSite.day);
            userVisit.setUser(userRepository.findById(userSite.id));
            userVisit.setUrl(userSite.url);
            userVisit.setAverage(userIndicators.timeSpent / userIndicators.visitQuantity);
            userVisitList.add(userVisit);
        }

        return userVisitList;
    }

    public UserVisit findByUserId(String userId) {
        return userVisitRepository.findByUserId(userId);
    }

    public List<UserVisit> findAllByUrl(String url) {
        return userVisitRepository.findAllByUrl(url);
    }
}
