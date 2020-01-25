package com.example.demo.repository;

import com.example.demo.entity.Mail;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MailRepository extends CrudRepository<Mail, Long> {

    List<Mail> findAll();
    Mail findByLogin(String login);
}
