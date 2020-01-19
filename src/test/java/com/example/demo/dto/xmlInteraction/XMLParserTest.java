package com.example.demo.dto.xmlInteraction;

import com.example.demo.UserDataGenerator;
import com.example.demo.dto.xmlStructure.input.Input;
import com.example.demo.dto.xmlStructure.input.Log;
import com.example.demo.entity.User;
import com.example.demo.entity.UserVisit;
import lombok.SneakyThrows;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class XMLParserTest {

    Logger log = Logger.getLogger(XMLParserTest.class.getName());

    @SneakyThrows
    @Test
    public void parseXMLTest() {
        int countDays = 3;
        int countUsers = 5;
        int countUserDuplicate = 2;
        LocalDateTime date = LocalDateTime.now();
        UserDataGenerator userDataGenerator = new UserDataGenerator();
        Input input = userDataGenerator.generateInput(countDays, countUsers, countUserDuplicate, "site", 10, date);
        Map<String, User> usersNameMap = userDataGenerator.getUsersNameMap();
        List<UserVisit> visits = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        for (Log log : input.getLogs()) {
            executorService.submit(new XMLParser(visits, usersNameMap, log));
        }

        executorService.shutdown();
        while (!executorService.isTerminated()) {
            Thread.sleep(3000);
        }

        assertEquals(countDays * countUsers * countUserDuplicate, visits.size());
    }

    @SneakyThrows
    @Test
    public void benchmarkTest() {
        int countDays = 5;
        int countUsers = 10000;
        int countUserDuplicate = 10;
        LocalDateTime date = LocalDateTime.now();
        UserDataGenerator userDataGenerator = new UserDataGenerator();
        Input input = userDataGenerator.generateInput(countDays, countUsers, countUserDuplicate, "site", 10, date);
        Map<String, User> usersNameMap = userDataGenerator.getUsersNameMap();
        List<UserVisit> visits = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        LocalDateTime startTime = LocalDateTime.now();
        for (Log log : input.getLogs()) {
            executorService.submit(new XMLParser(visits, usersNameMap, log));
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            Thread.sleep(3000);
        }
        Duration betweenOneThread = Duration.between(startTime, LocalDateTime.now());

        visits.clear();

        executorService = Executors.newFixedThreadPool(10);
        startTime = LocalDateTime.now();
        for (Log log : input.getLogs()) {
            executorService.submit(new XMLParser(visits, usersNameMap, log));
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            Thread.sleep(3000);
        }
        Duration betweenTenThread = Duration.between(startTime, LocalDateTime.now());

        log.info("One thread: " + betweenOneThread.getSeconds()
                + "; Ten thread: " + betweenTenThread.getSeconds());
        assertTrue(betweenOneThread.getSeconds() / betweenTenThread.getSeconds() > 4);
    }
}