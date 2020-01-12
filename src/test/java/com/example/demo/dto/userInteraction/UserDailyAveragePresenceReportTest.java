package com.example.demo.dto.userInteraction;

import com.example.demo.UserDataGenerator;
import com.example.demo.entity.User;
import com.example.demo.entity.UserVisit;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class UserDailyAveragePresenceReportTest {

    @Test
    public void getGroupUserTest() {
        int timeSpent = 10;
        UserDataGenerator userDataGenerator = new UserDataGenerator();
        List<UserVisit> userVisits = userDataGenerator.generateUserVisits(5, 1, timeSpent, "site");
        Map<Integer, User> usersIdMap = userDataGenerator.getUsersIdMap();

        UserDailyAveragePresenceReport averagePresenceReport = new UserDailyAveragePresenceReport(usersIdMap, userVisits);
        List<UserAveragePresence> visits = averagePresenceReport.getGroupUsers();

        assertEquals(1, visits.size());
        assertEquals(timeSpent, visits.get(0).average.intValue());
    }

    @Test
    public void getGroupUsersTest() {
        UserDataGenerator userDataGenerator = new UserDataGenerator();
        List<UserVisit> userVisits = userDataGenerator.generateUserVisits(20, 4, "site");
        Map<Integer, User> usersIdMap = userDataGenerator.getUsersIdMap();

        UserDailyAveragePresenceReport averagePresenceReport = new UserDailyAveragePresenceReport(usersIdMap, userVisits);
        List<UserAveragePresence> visits = averagePresenceReport.getGroupUsers();

        assertEquals(4, visits.size());
    }
}