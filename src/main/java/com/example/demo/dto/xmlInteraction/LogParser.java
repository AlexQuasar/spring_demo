package com.example.demo.dto.xmlInteraction;

import com.example.demo.dto.xmlInteraction.interfaces.Parser;
import com.example.demo.dto.xmlStructure.input.Input;
import com.example.demo.dto.userInteraction.UserIndicators;
import com.example.demo.dto.userInteraction.UserSite;
import com.example.demo.entity.User;
import com.example.demo.entity.UserVisit;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class LogParser implements Parser<Input, List<UserVisit>> {

    Map<Integer, User> usersIdMap;

    @Override
    public List<UserVisit> parse(Input input) {
        XMLParser xmlParser = new XMLParser(usersIdMap);
        xmlParser.parseXML(input);

        Map<LocalDate, Map<UserSite, UserIndicators>> dateUserMap = xmlParser.getVisitsMap();

        List<UserVisit> visits = new ArrayList<>();
        for (Map.Entry<LocalDate, Map<UserSite, UserIndicators>> entryDate : dateUserMap.entrySet()) {
            for (Map.Entry<UserSite, UserIndicators> entry : entryDate.getValue().entrySet()) {
                UserSite userSite = entry.getKey();
                UserIndicators userIndicators = entry.getValue();
                UserVisit userVisit = new UserVisit();
                userVisit.setDay(entryDate.getKey());
                userVisit.setUser(usersIdMap.get(userSite.user_id));
                userVisit.setUrl(userSite.url);
                userVisit.setTimeSpent(userIndicators.timeSpent);
                userVisit.setTimeInterval(userIndicators.timeInterval);

                visits.add(userVisit);
            }
        }

        return visits;
    }
}
