package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(schema = "public", name = "user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserVisit> userVisits;

    @JsonIgnore
    // TODO: 1/8/20 вместо этого уже есть геттер
    // тут я поставил игнор, чтобы он JSON не зацикливал. у меня есть один запрос в котором возвращаются сгруппированные UserVisit
    // а у нас же тут связь OneToMany и ManyToOne
    public Set<UserVisit> getUserVisits() {
        return userVisits;
    }
}
