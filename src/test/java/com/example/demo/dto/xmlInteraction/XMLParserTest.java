package com.example.demo.dto.xmlInteraction;

import com.example.demo.UserDataGenerator;
import com.example.demo.dto.userInteraction.UserIndicators;
import com.example.demo.dto.userInteraction.UserSite;
import com.example.demo.dto.xmlStructure.input.Input;
import com.example.demo.entity.User;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.Assert.*;

public class XMLParserTest {

    @Test
    public void parseXMLTest() {
        int countDays = 3;
        int countUsers = 5;
        LocalDateTime date = LocalDateTime.now();
        UserDataGenerator userDataGenerator = new UserDataGenerator();
        Input input = userDataGenerator.generateInput(countDays, countUsers, "site", date);
        Map<Integer, User> usersIdMap = userDataGenerator.getUsersIdMap();

        XMLParser xmlParser = new XMLParser(usersIdMap);
        xmlParser.parseXML(input);
        Map<LocalDate, Map<UserSite, UserIndicators>> visitsMap = xmlParser.getVisitsMap();

        assertEquals(countDays, visitsMap.size());
        assertEquals(countUsers, visitsMap.get(date.toLocalDate()).values().size());
    }
}