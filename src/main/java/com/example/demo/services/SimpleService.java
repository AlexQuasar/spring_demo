package com.example.demo.services;

import com.example.demo.entity.UserTest;
import com.example.demo.repository.UsersRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class SimpleService {

    private UsersRepository usersRepository;

    public SimpleService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @PostConstruct
    public void init(){
        Iterable<UserTest> all = usersRepository.findAll();
        all.forEach(System.out::println); // TODO: 11/21/19 put a breakpoint and debug start
    }

    public void saveUser(UserTest user) {
        usersRepository.save(user);
    }

    public List<UserTest> getAllUsersByName(String name) {
        return usersRepository.findAllByName(name);
    }
}
