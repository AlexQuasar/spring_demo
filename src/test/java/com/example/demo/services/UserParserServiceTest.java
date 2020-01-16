package com.example.demo.services;

import com.example.demo.UserDataGenerator;
import com.example.demo.dto.xmlStructure.input.Input;
import com.example.demo.entity.UserVisit;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserVisitRepository;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserParserServiceTest {

    UserVisitRepository userVisitRepository;
    UserRepository userRepository;
    UserParserService userParserService;

    @Before
    public void setUp() {
        userVisitRepository = mock(UserVisitRepository.class);
        userRepository = mock(UserRepository.class);
        userParserService = new UserParserService(userVisitRepository, userRepository);
    }

    @Test
    public void addVisitTest() {
        UserDataGenerator userDataGenerator = new UserDataGenerator();
        UserVisit userVisit = userDataGenerator.generateUserVisit(1, LocalDateTime.now(), 1, 100, "site");

//        userVisitRepository.deleteAll();
        userParserService.addVisit(userVisit);

        // не могу разобраться как кастовать в нужные мне классы когда вызываешь метод mock класса (userVisitRepository.findAll(), например)
        List<UserVisit> visits = when(userVisitRepository.findAll()).getMock();
        assertEquals(1, visits.size());
    }

    @Test
    public void addVisitsTest() {
    }

    @Test
    public void getGroupedUserVisitsTest() {
    }

    @Test
    public void addLogsTest() {
        int countDays = 3;
        int countUsers = 5;
        LocalDateTime date = LocalDateTime.now();
        UserDataGenerator userDataGenerator = new UserDataGenerator();
        Input input = userDataGenerator.generateInput(countDays, countUsers, "site", date);
    }
}