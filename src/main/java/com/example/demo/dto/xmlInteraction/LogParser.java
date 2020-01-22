package com.example.demo.dto.xmlInteraction;

import com.example.demo.dto.xmlInteraction.interfaces.Parser;
import com.example.demo.dto.xmlStructure.input.Input;
import com.example.demo.dto.xmlStructure.input.Log;
import com.example.demo.entity.User;
import com.example.demo.entity.UserVisit;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class LogParser implements Parser<Input, List<UserVisit>> {

    private static int countThread = 10;
    Map<String, User> usersNameMap;

    @SneakyThrows
    @Override
    public List<UserVisit> parse(Input input) {
        List<UserVisit> visits = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(countThread);

        for (Log log : input.getLogs()) {
            executorService.submit(new XMLParser(visits, usersNameMap, log));
        }

        executorService.shutdown();
        boolean isNotTerminated = true;
        // сделал так, но складывается такое ощущение что он нифига не дожидается, когда данные обрабатываются быстро
        // не могу разобраться
        // TODO: 1/21/20 стоит прочитать javadoc что этот метод делает что принимает в качестве аргументов и что возвращает
        //  мне кажется све эти 3 вещи от тебя ускользают. В крайнем случае есть еще один способ
        // пробовал через CountDownLatch - не получилось
        // с invokeAll() не разобрался как дожидаться всех потоков
        // ExecutorCompletionService вообще не понял
        // поэтому сделал как смог, с остальным, как написал выше "не могу разобраться"
        while (isNotTerminated) {
            isNotTerminated = !executorService.awaitTermination(3, TimeUnit.SECONDS);
        }

        return visits;
    }
}
