package com.example.demo.entity;

import com.example.demo.dto.mailInteraction.DataMail;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(schema = "public", name = "mail")
@NoArgsConstructor
@Getter
@Setter
public class Mail {

    // TODO: 1/26/20 почему mail. У тебя сервис по учету времени пребывания на сайтах. У него должен быть барьер
    //  чтобы предоставлять данные избирательно. Соответственно нужен список администраторов у которых есть доступ. Список поплняемый.
    // тут немного не понял "У него должен быть барьер чтобы предоставлять данные избирательно" - добавить поле с аналитикой?
    // и что значит "нужен список администраторов"? тут же почта хранится
    // TODO: 1/29/20 логин это же не всегда почта. Это может быть просто никнейм.
    // все равно ничего не понял. можно поподробнее описать что я не так сделал тут

    public Mail(DataMail dataMail) {
        this.login = dataMail.getLogin();
        this.password = dataMail.getPassword();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;
}
