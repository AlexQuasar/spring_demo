package com.example.demo.services;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserParserService {

    private UserRepository userRepository;
    private UserService userService = new UserService();
    private List<User> users = new ArrayList<>();

    public UserParserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUsers(List<User> users) {
        for (User user : users) {
            addUser(user);
        }
    }

    public void addUser(User user) {
        users.add(user);
    }

    public List<User> getSortedUsers() {
        if (users.isEmpty()) {
            return new ArrayList<>();
        }
        sortUsers();
        return userRepository.findAll();
    }

    private void sortUsers() {
        List<User> userList = userService.sortUsers(users);
        userRepository.saveAll(userList);
    }

    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    public List<User> findAllByUrl(String url) {
        return userRepository.findAllByUrl(url);
    }
}
