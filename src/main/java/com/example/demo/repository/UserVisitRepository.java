package com.example.demo.repository;

import com.example.demo.entity.User;
import com.example.demo.entity.UserVisit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserVisitRepository extends CrudRepository<UserVisit, Integer> {

    List<UserVisit> findAll();
    List<UserVisit> findAllByUserAndDay(User user, LocalDate date);
}
