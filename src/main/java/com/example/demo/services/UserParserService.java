package com.example.demo.services;

import com.example.demo.dto.userInteraction.UserAveragePresence;
import com.example.demo.dto.userInteraction.UserDailyAveragePresenceReport;
import com.example.demo.dto.xmlInteraction.LogParser;
import com.example.demo.dto.xmlStructure.input.Input;
import com.example.demo.entity.User;
import com.example.demo.entity.UserVisit;
import com.example.demo.repository.UserVisitRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserParserService {

    // TODO: 1/14/20 не могу понять как тесты писать на этот класс, потому что тут репозитории тянутся.
    //  мне для тестов этого класса надо спринг поднимать или как-то имитировать репозитории?
    private UserVisitRepository userVisitRepository;
    private UserRepository userRepository;

    public UserParserService(UserVisitRepository userVisitRepository, UserRepository userRepository) {
        this.userVisitRepository = userVisitRepository;
        this.userRepository = userRepository;
    }

    public void addVisit(UserVisit userVisit) {
        userVisitRepository.save(userVisit);
    }

    public void addVisits(List<UserVisit> userVisits) {
        userVisitRepository.saveAll(userVisits);
    }

    public List<UserAveragePresence> getGroupedUserVisits() {
        UserDailyAveragePresenceReport averagePresenceReport = new UserDailyAveragePresenceReport(getUsersNameMap(userRepository.findAll()), userVisitRepository.findAll());
        return averagePresenceReport.getGroupUsers();
    }

    public void addLogs(Input input) {
        // TODO: 1/8/20 репозиторий передавать не нужно, просто нужно смаппить один дто в аналогичный ентити, работа с базой вся здесь.
        // а как тогда сделать? потому что у меня в классе XMLParser есть строка "userRepository.save(user);". как поддерживать актуальные данные в репозитории не передавая его?
        // TODO: 1/8/20 тебе прихоят логи которые содержат информацию о визитах пользователей, их нужно преобразовать в объекты UserVisit
        //  для этого репозиторий не нужен,
        // я его там использую потому что в одном логе может быть два посещения: на текущий день и на следующий
        // TODO: 1/9/20 и зачем нужен репозиторий, чтобы разбить 1 Log на n UserVisits ?
        // в предыдущем комменте не правильно выразился. вообще мне там этот репозиторий нужен для того, что если такого User'а из Log нет в таблице, то сразу его добавить
        // TODO: 1/10/20 боюсь что в таком случае это нарушение Single responsibility principle. Можно либо сделать здесь же UserExtractor, например, и вызывать его отдельно
        //  чтобы получить список юзеров. Но штука в том что все новые юзеры и так создадутся в БД, когда ты будешь записывать твои визиты каскадом, правда тут возможны подводные камни
        //  но с ними как раз лучше как следует поразбираться, надо бы все-таки сделать username уникальным полем и переопределить на уровне БД у его объектов equals по этому свойству,
        //  я бы копал в эту степь. Если не получится, то первый вариант тоже приемлем.
        // сделал, чтобы репозитории не передавались, но теперь есть небольшой нюанс - тот id, с которым они приходят из xml, теряется
        // и им, при записи в репозиторий, уже присваивается другой совсем id
        // TODO: 1/12/20 если исходить из того что username уникален, то я бы поле id из request вообще бы убрал
        LogParser logParser = new LogParser(getUsersNameMap(userRepository.findAll()));
        List<UserVisit> visits = logParser.parse(input);
        userVisitRepository.saveAll(visits);
    }

    private Map<String, User> getUsersNameMap(List<User> users) {
        Map<String, User> usersNameMap = new HashMap<>();
        users.forEach(i -> usersNameMap.put(i.getName(), i));
        return usersNameMap;
    }
}
