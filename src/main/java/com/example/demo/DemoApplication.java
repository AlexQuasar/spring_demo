package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
//        ApplicationContext context = SpringApplication.run(DemoApplication.class, args);
//        UsersRepository usersRepository =(UsersRepository) context.getBean("usersRepository");
//        Iterable<User> all = usersRepository.findAll();

    }

//    public static void main(String[] args) {
//        Locale aDefault = Locale.getDefault();
//        System.out.println(aDefault);
//    }
}
