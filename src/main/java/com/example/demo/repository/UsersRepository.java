package com.example.demo.repository;

import com.example.demo.entity.UserTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends CrudRepository<UserTest, Integer> {

    List<UserTest> findAllByName(String name);
}
