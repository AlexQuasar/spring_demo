package com.example.demo.dto.xmlInteraction;

import com.example.demo.dto.xmlStructure.input.Input;
import com.example.demo.dto.userInteraction.UserIndicators;
import com.example.demo.dto.userInteraction.UserSite;
import com.example.demo.entity.User;
import com.example.demo.entity.UserVisit;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class LogParser {

    UserRepository userRepository;
    Input input;

    public List<UserVisit> parse() {
        XMLParser xmlParser = new XMLParser(userRepository);
        xmlParser.parseXML(input);

        Map<LocalDate, Map<UserSite, UserIndicators>> dateUserMap = xmlParser.getVisitsMap();

        List<UserVisit> visits = new ArrayList<>();
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
                userVisit.setTimeInterval(userIndicators.timeInterval);

                visits.add(userVisit);
            }
        }

        return visits;
    }
}
