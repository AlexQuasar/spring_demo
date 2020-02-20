package com.example.demo.services;

import com.example.demo.dto.mailInteraction.DataMail;
import com.example.demo.TokenGenerator;
import com.example.demo.entity.Mail;
import com.example.demo.entity.User;
import com.example.demo.entity.UserVisit;
import com.example.demo.repository.MailRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserVisitRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

    @Mock
    MailRepository mailRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    UserVisitRepository userVisitRepository;

    @InjectMocks
    AuthenticationService authenticationService;

    DataMail dataMail;
    User user;

    final long hundredYearsInSeconds = 3155673600L;

    @Test
    public void registration() {
        initialSettings();

        when(userRepository.findByName(anyString())).thenReturn(user);

        assertTrue(authenticationService.registration(dataMail));
        verify(mailRepository).findByLogin(anyString());
        verify(userRepository).findByName(anyString());
        verify(mailRepository).save(any(Mail.class));
    }

    @Test
    public void authorization() {
        initialSettings();
        Mail mail = new Mail(dataMail, user);

        when(mailRepository.findByLogin(dataMail.getLogin())).thenReturn(mail);

        // TODO: 2/16/20 тут ошибка из-за того что в классе TokenGenerator одно поле заполняется когда поднимается контекст - "${authentication.delay}"
        //  как обойти это при юнит-тестах?
        // TODO: 2/19/20 не факт что это оптимальный подход в данной ситуации, но инструмент рекомендую освоить Reflection в тестах пригодится,
        //  пользуясь им можно сетать приватные поля снаружи. https://javarush.ru/groups/posts/513-reflection-api-refleksija-temnaja-storona-java
        int delay = 5;
        assertTrue(setDelayPrivateField(delay));

        assertTrue(authenticationService.authorization(dataMail.getLogin(), dataMail.getPassword()));
        verify(mailRepository).findByLogin(anyString());
    }

    @Test
    public void getTodayVisits() {
        initialSettings();
        Mail mail = new Mail(dataMail, user);

        UserVisit userVisit = new UserVisit();
        userVisit.setUser(user);
        List<UserVisit> visits = Arrays.asList(userVisit);

        when(mailRepository.findByLogin(anyString())).thenReturn(mail);
        when(userRepository.findById(anyInt())).thenReturn(user);
        when(userVisitRepository.findAllByUserAndDay(any(User.class), any(LocalDate.class))).thenReturn(visits);

        TokenGenerator tokenGenerator = new TokenGenerator(hundredYearsInSeconds);
        String token = tokenGenerator.generateToken(dataMail.getLogin());

        List<UserVisit> userVisits = authenticationService.getTodayVisits(token);

        assertEquals(visits.size(), userVisits.size());
    }

    private void initialSettings() {
        dataMail = new DataMail("test_mail@google.com", "12345", "test_user");
        user = new User();
        user.setName(dataMail.getUserName());
    }

    private Boolean setDelayPrivateField(int delay) {
        try {
            Field field = authenticationService.getClass().getDeclaredField("delay");
            field.setAccessible(true);
            field.set(authenticationService, delay);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            return false;
        }
        return true;
    }
}