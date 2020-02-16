package com.example.demo.services;

import com.example.demo.dto.userInteraction.UserAveragePresence;
import com.example.demo.dto.userInteraction.UserDailyAveragePresenceReport;
import com.example.demo.dto.xmlInteraction.LogParser;
import com.example.demo.dto.xmlStructure.input.Log;
import com.example.demo.entity.User;
import com.example.demo.entity.UserVisit;
import com.example.demo.repository.UserVisitRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.web.output.JSONPlaceHolderClient;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserParserService {

    private UserVisitRepository userVisitRepository;
    private UserRepository userRepository;
    private JSONPlaceHolderClient jsonPlaceHolderClient;

    @PostConstruct
    public void init(){
        String dummy = jsonPlaceHolderClient.getDummy();
        System.out.println(dummy);
    }

    public UserParserService(UserVisitRepository userVisitRepository, UserRepository userRepository, JSONPlaceHolderClient jsonPlaceHolderClient) {
        this.userVisitRepository = userVisitRepository;
        this.userRepository = userRepository;
        this.jsonPlaceHolderClient = jsonPlaceHolderClient;
    }

    public void addVisit(UserVisit userVisit) {
        userVisitRepository.save(userVisit);
    }

    public void addVisits(List<UserVisit> userVisits) {
        userVisitRepository.saveAll(userVisits);
    }

    public List<UserAveragePresence> getGroupedUserVisits() {
        UserDailyAveragePresenceReport averagePresenceReport = new UserDailyAveragePresenceReport(getUsersNameMap(userRepository.findAll()), userVisitRepository.findAll());
        return averagePresenceReport.getGroupUsers();
    }

    public void addLog(List<Log> logs) {
        LogParser logParser = new LogParser(getUsersNameMap(userRepository.findAll()));
        List<UserVisit> visits = logParser.parse(logs);
        userVisitRepository.saveAll(visits);
    }

    private Map<String, User> getUsersNameMap(List<User> users) {
        Map<String, User> usersNameMap = new HashMap<>();
        users.forEach(i -> usersNameMap.put(i.getName(), i));
        return usersNameMap;
    }
}
