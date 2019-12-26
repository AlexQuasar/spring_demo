package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.repo.UsersRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Locale;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        UsersRepository usersRepository =(UsersRepository) context.getBean("usersRepository");
        Iterable<User> all = usersRepository.findAll();

    }

//    public static void main(String[] args) {
//        Locale aDefault = Locale.getDefault();
//        System.out.println(aDefault);
//    }
}
