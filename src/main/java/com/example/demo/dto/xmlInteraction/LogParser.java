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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@AllArgsConstructor
public class LogParser implements Parser<Input, List<UserVisit>> {

	private static int countThread = 10;
	Map<String, User> usersNameMap;

	/**
	 * Первый правильный способ
	 */
	public static void main(String[] args) throws InterruptedException, TimeoutException {

		ExecutorService executorService = Executors.newFixedThreadPool(10);
		for (int i = 0; i < 10; i++) {
			executorService.submit(() -> runForever());
		}
		executorService.shutdown();
		if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
		    executorService.shutdownNow();
			throw new TimeoutException("Executor stopped before finishing all tasks after 1 minute timeout");
		}
		System.out.println("Main done!");

	}

	/**
	 * Второй правильный способ
	 */
	public static void main2(String[] args) throws InterruptedException {

		ExecutorService executorService = Executors.newFixedThreadPool(10);
		List<Callable<Object>> tasks = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Runnable runnable = () -> run();
			Callable<Object> callable = Executors.callable(runnable);
			tasks.add(callable);
		}
		executorService.invokeAll(tasks);
		executorService.shutdown();
		System.out.println("Main done!");

	}

	@SneakyThrows
	private static void run() {
		Thread.sleep(10000);
		System.out.println(Thread.currentThread() + "Done!");
	}

	@SneakyThrows
	private static void runForever() {
        while (true) {
            Thread.sleep(10000);
        }
	}

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
		// TODO: 1/23/20 когда не можешь разобраться вынимай из контекста задачу и реализуй ее изолированно
		while (isNotTerminated) {
			isNotTerminated = !executorService.awaitTermination(3, TimeUnit.SECONDS);
		}

		return visits;
	}


}
