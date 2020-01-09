package com.example.demo.services;

import com.example.demo.dto.userInteraction.UserDailyAveragePresenceReport;
import com.example.demo.dto.xmlInteraction.LogParser;
import com.example.demo.dto.xmlStructure.input.Input;
import com.example.demo.entity.UserVisit;
import com.example.demo.repository.UserVisitRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserParserService {

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

    public List<UserVisit> getGroupedUserVisits() {
        UserDailyAveragePresenceReport averagePresenceReport = new UserDailyAveragePresenceReport(userRepository, userVisitRepository.findAll());
        return averagePresenceReport.getGroupUsers();
    }

    public void addLogs(Input input) {
        // TODO: 1/8/20 репозиторий передавать не нужно, просто нужно смаппить один дто в аналогичный ентити, работа с базой вся здесь.
        // а как тогда сделать? потому что у меня в классе XMLParser есть строка "userRepository.save(user);". как поддерживать актуальные данные в репозитории не передавая его?
        // TODO: 1/8/20 тебе прихоят логи которые содержат информацию о визитах пользователей, их нужно преобразовать в объекты UserVisit
        //  для этого репозиторий не нужен,
        // я его там использую потому что в одном логе может быть два посещения: на текущий день и на следующий
        LogParser logParser = new LogParser(userRepository, input);
        List<UserVisit> visits = logParser.parse();
        userVisitRepository.saveAll(visits);
    }
}
