package com.example.demo.services;

import com.example.demo.UserDataGenerator;
import com.example.demo.dto.userInteraction.UserAveragePresence;
import com.example.demo.dto.xmlStructure.input.Log;
import com.example.demo.entity.UserVisit;
import com.example.demo.repository.UserVisitRepository;
import com.example.demo.web.output.UserClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserParserServiceTest {

    @Mock
    UserVisitRepository userVisitRepository;

    @Mock
    UserClient userClient;

    @InjectMocks
    UserParserService userParserService;


    @Test
    public void addVisitTest() {
        UserDataGenerator userDataGenerator = new UserDataGenerator();
        UserVisit userVisit = userDataGenerator.generateUserVisit(1, LocalDateTime.now(), 1, 100, "site");

        userParserService.addVisit(userVisit);

        verify(userVisitRepository).save(userVisit);
    }

    @Test
    public void addVisitsTest() {
        UserDataGenerator userDataGenerator = new UserDataGenerator();
        List<UserVisit> visits = userDataGenerator.generateUserVisits(5, 1, 100, "site");

        userParserService.addVisits(visits);

        verify(userVisitRepository).saveAll(visits);
    }

    @Test
    public void getGroupedUserVisitsTest() {
        int countVisits = 5;
        int countUsers = 1;
        int timeSpent = 10;
        UserDataGenerator userDataGenerator = new UserDataGenerator();
        List<UserVisit> visits = userDataGenerator.generateUserVisits(countVisits, countUsers, timeSpent, "site");
        when(userClient.findAll()).thenReturn(userDataGenerator.users);
        when(userVisitRepository.findAll()).thenReturn(visits);

        List<UserAveragePresence> groupedUserVisits = userParserService.getGroupedUserVisits();

        assertEquals(countUsers, groupedUserVisits.size());
        assertEquals(timeSpent, groupedUserVisits.get(0).getAverage().intValue());
    }

    @Test
    public void addLogTest() {
        int countDays = 3;
        int countUsers = 5;
        LocalDateTime date = LocalDateTime.now();
        UserDataGenerator userDataGenerator = new UserDataGenerator();
        List<Log> logs = userDataGenerator.generateLogs(1, countDays, countUsers, "site", date);

        userParserService.addLog(logs);

        verify(userClient).findAll();
        verify(userVisitRepository).saveAll(anyList());
    }
}