package com.example.demo.dto.xmlInteraction;

import com.example.demo.UserDataGenerator;
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
import java.util.concurrent.Callable;
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
        Log log = userDataGenerator.generateLog(countDays, countUsers, countUserDuplicate, "site", 10, date);
        Map<String, User> usersNameMap = userDataGenerator.getUsersNameMap();
        List<UserVisit> visits = new ArrayList<>();

        XMLParser xmlParser = new XMLParser(visits, usersNameMap, log);
        Thread thread = new Thread(xmlParser);
        thread.start();
        thread.join();

        assertEquals(countDays * countUsers * countUserDuplicate, visits.size());
    }

    @SneakyThrows
    @Test
    public void benchmarkTest() {
        int countLogs = 100;
        int countDays = 5;
        int countUsers = 100;
        int countUserDuplicate = 1;
        LocalDateTime date = LocalDateTime.now();
        UserDataGenerator userDataGenerator = new UserDataGenerator();
        List<Log> logs = userDataGenerator.generateLogs(countLogs, countDays, countUsers, countUserDuplicate, "site", 10, date);
        Map<String, User> usersNameMap = userDataGenerator.getUsersNameMap();
        List<UserVisit> visits = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        List<Callable<Object>> tasks = new ArrayList<>();
        LocalDateTime startTime = LocalDateTime.now();
        for (Log log : logs) {
            XMLParser xmlParser = new XMLParser(visits, usersNameMap, log);
            Callable<Object> callable = Executors.callable(xmlParser);
            tasks.add(callable);
        }
        executorService.invokeAll(tasks);
        executorService.shutdown();
        Duration betweenOneThread = Duration.between(startTime, LocalDateTime.now());

        visits.clear();
        tasks.clear();

        executorService = Executors.newFixedThreadPool(10);
        startTime = LocalDateTime.now();
        for (Log log : logs) {
            XMLParser xmlParser = new XMLParser(visits, usersNameMap, log);
            Callable<Object> callable = Executors.callable(xmlParser);
            tasks.add(callable);
        }
        executorService.invokeAll(tasks);
        executorService.shutdown();
        Duration betweenTenThread = Duration.between(startTime, LocalDateTime.now());

        this.log.info("One thread: " + betweenOneThread.getSeconds()
                + "; Ten thread: " + betweenTenThread.getSeconds());
        assertTrue(betweenOneThread.getSeconds() / (betweenTenThread.getSeconds() == 0 ? 1 : betweenTenThread.getSeconds()) > 4);
    }
}