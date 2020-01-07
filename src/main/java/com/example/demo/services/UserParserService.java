package com.example.demo.services;

import com.example.demo.dto.XMLInteraction.XMLParser;
import com.example.demo.dto.structureXML.input.Input;
import com.example.demo.dto.support.UserIndicators;
import com.example.demo.dto.support.UserSite;
import com.example.demo.entity.User;
import com.example.demo.entity.UserVisit;
import com.example.demo.repository.UserVisitRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
                userIndicators.timeSpent += userVisit.getTimeSpent();
            } else {
                userSiteMap.put(userSite, new UserIndicators(userVisit.getId(), userVisit.getTimeSpent()));
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
            userVisitList.add(userVisit);
        }

        return userVisitList;
    }

    public void addLogs(Input input) {
        XMLParser xmlParser = new XMLParser(userRepository);
        xmlParser.parseXML(input);

        Map<LocalDate, Map<UserSite, UserIndicators>> dateUserMap = xmlParser.getVisitsMap();
        collectUserVisits(dateUserMap);
    }

    private void collectUserVisits(Map<LocalDate, Map<UserSite, UserIndicators>> dateUserMap) {
        for (Map.Entry<LocalDate, Map<UserSite, UserIndicators>> entryDate : dateUserMap.entrySet()) {
            for (Map.Entry<UserSite, UserIndicators> entry : entryDate.getValue().entrySet()) {
                UserSite userSite = entry.getKey();
                UserIndicators userIndicators = entry.getValue();
                User user = userRepository.findById(userSite.user_id);

                UserVisit userVisit = new UserVisit();
                userVisit.setDay(entryDate.getKey());
                userVisit.setUser(user);
                userVisit.setUrl(userSite.url);
                userVisit.setTimeSpent(userIndicators.timeSpent);

                // при добавлении сыпется 500 ошибка "could not execute statement; SQL [n/a]". пока не разобрался в чем проблема
                // вот полная ошибка из Postman "could not execute statement; SQL [n/a]; nested exception is org.hibernate.exception.SQLGrammarException: could not execute statement"
                // не могу понять где грамматическая ошибка и она ли это вообще
                addVisit(userVisit);
            }
    }
    }

    public void addVisits(Input input) {
        LogParser logParser = new LogParser(input); // TODO: 1/7/20 implement LogParser
        List<UserVisit> visits = logParser.parse();
        userVisitRepository.saveAll(visits);
    }
}
