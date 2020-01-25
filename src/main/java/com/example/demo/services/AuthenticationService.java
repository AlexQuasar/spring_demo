package com.example.demo.services;

import com.example.demo.entity.Mail;
import com.example.demo.repository.MailRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private MailRepository mailRepository;

    public AuthenticationService(MailRepository mailRepository) {
        this.mailRepository = mailRepository;
    }

    public Boolean registration(Mail newMail) {
        Mail mail = mailRepository.findByLogin(newMail.getLogin());
        if (mail == null) {
            mailRepository.save(newMail);
            return true;
        }

        return false;
    }
}
