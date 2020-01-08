package com.example.demo.entity;

import com.vladmihalcea.hibernate.type.interval.PostgreSQLIntervalType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;

@Entity
@Table(schema = "public", name = "user_visit")
@TypeDef(typeClass = PostgreSQLIntervalType.class, defaultForType = Duration.class)
@Getter
@Setter
public class UserVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "day")
    private LocalDate day;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "url")
    private String url;

    @Column(name = "time_spent")
    private Integer timeSpent = 0;

    @Column(name = "time_interval")
    private Duration timeInterval;
}
