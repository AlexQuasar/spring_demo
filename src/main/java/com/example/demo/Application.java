package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class Application {

	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
//        ApplicationContext context = SpringApplication.run(DemoApplication.class, args);
//        UsersRepository usersRepository =(UsersRepository) context.getBean("usersRepository");
//        Iterable<User> all = usersRepository.findAll();
    }

//    public static void main(String[] args) {
//        Locale aDefault = Locale.getDefault();
//        System.out.println(aDefault);
//    }
}
