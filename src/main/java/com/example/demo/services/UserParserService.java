package com.example.demo.services;

import com.example.demo.dto.userInteraction.UserAveragePresence;
import com.example.demo.dto.userInteraction.UserDailyAveragePresenceReport;
import com.example.demo.dto.xmlInteraction.LogParser;
import com.example.demo.dto.xmlStructure.input.Log;
import com.example.demo.entity.User;
import com.example.demo.entity.UserVisit;
import com.example.demo.repository.UserVisitRepository;
import com.example.demo.web.output.UserClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserParserService {

    private UserVisitRepository userVisitRepository;
    private UserClient userClient;

    public UserParserService(UserVisitRepository userVisitRepository, UserClient userClient) {
        this.userVisitRepository = userVisitRepository;
        this.userClient = userClient;
    }

    public void addVisit(UserVisit userVisit) {
        userVisitRepository.save(userVisit);
    }

    public void addVisits(List<UserVisit> userVisits) {
        userVisitRepository.saveAll(userVisits);
    }

    public List<UserAveragePresence> getGroupedUserVisits() {
        UserDailyAveragePresenceReport averagePresenceReport = new UserDailyAveragePresenceReport(getUsersNameMap(userClient.findAll()), userVisitRepository.findAll());
        return averagePresenceReport.getGroupUsers();
    }

    public void addLog(List<Log> logs) {
        LogParser logParser = new LogParser(getUsersNameMap(userClient.findAll()));
        List<UserVisit> visits = logParser.parse(logs);
        userVisitRepository.saveAll(visits);
    }

    private Map<String, User> getUsersNameMap(List<User> users) {
        Map<String, User> usersNameMap = new HashMap<>();
        users.forEach(i -> usersNameMap.put(i.getName(), i));
        return usersNameMap;
    }
}
