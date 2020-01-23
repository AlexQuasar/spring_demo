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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@AllArgsConstructor
public class LogParser implements Parser<Input, List<UserVisit>> {

	private static int countThread = 10;
	Map<String, User> usersNameMap;

	@SneakyThrows
	@Override
	public List<UserVisit> parse(Input input) {
		List<UserVisit> visits = new ArrayList<>();
		ExecutorService executorService = Executors.newFixedThreadPool(countThread);
        List<Callable<Object>> tasks = new ArrayList<>();

		for (Log log : input.getLogs()) {
		    XMLParser xmlParser = new XMLParser(visits, usersNameMap, log);
			Callable<Object> callable = Executors.callable(xmlParser);
			tasks.add(callable);
		}
		executorService.invokeAll(tasks);
		executorService.shutdown();

		return visits;
	}
}
