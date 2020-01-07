package com.example.demo.repository;

import com.example.demo.entity.UserVisit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserVisitRepository extends CrudRepository<UserVisit, Long> {

    UserVisit findByUserId(String userId);
    List<UserVisit> findAllByUrl(String url);
    List<UserVisit> findAll();
}
