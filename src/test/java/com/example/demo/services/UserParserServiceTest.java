package com.example.demo.services;

import com.example.demo.UserDataGenerator;
import com.example.demo.dto.userInteraction.UserAveragePresence;
import com.example.demo.dto.xmlStructure.input.Input;
import com.example.demo.entity.UserVisit;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserVisitRepository;
import org.junit.Before;
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
    UserRepository userRepository;

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
        UserDataGenerator userDataGenerator = new UserDataGenerator();
        List<UserVisit> visits = userDataGenerator.generateUserVisits(1, 1, 10, "site");
        when(userRepository.findAll()).thenReturn(userDataGenerator.users);
        when(userVisitRepository.findAll()).thenReturn(visits);
        // TODO: 1/23/20 сначала задаешь поведение потом вызываешь тестируемый метод
        List<UserAveragePresence> groupedUserVisits = userParserService.getGroupedUserVisits();

        // TODO: 1/20/20 оригинально, но во-первых тогда уж так assertTrue(groupedUserVisits.isEmpty());, а во вторых я бы использовал verify, например,
        //  в методах delete или save а вот в findAll как раз следует какие-то данные вернуть через when и проверить то ли нам пришло в итоге
        //  из getGroupedUserVisits
        // в том то и дело, что я ведь не знаю какие данные мне сюда приходят, как их проверить через when? этого я не смог пока понять
        // TODO: 1/21/20 when не чтобы проверять, а чтобы подменять создаваемые фейковые данные https://www.baeldung.com/mockito-behavior
        // как я это понял. но зачем тогда нам тут вообще "userParserService.getGroupedUserVisits()"?


        // TODO: 1/23/20 в итоге ожидаешь что данные не пустые, хотя бы, но желательно больше ассертов. Только не тех что ты написал.
        assertFalse(groupedUserVisits.isEmpty());

    }

    @Test
    public void addLogsTest() {
        int countDays = 3;
        int countUsers = 5;
        LocalDateTime date = LocalDateTime.now();
        UserDataGenerator userDataGenerator = new UserDataGenerator();
        Input input = userDataGenerator.generateInput(countDays, countUsers, "site", date);

        userParserService.addLogs(input);

        verify(userRepository).findAll();
        verify(userVisitRepository).saveAll(any());
    }
}