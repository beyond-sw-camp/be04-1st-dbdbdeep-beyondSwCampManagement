package com.dbdbdeep.dbdbdeep;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Money {
    @Id
    @Column(name = "학번")
    private Integer 학번;

    @Column
    private String std_name;

    @Column(name = "지원금산정")
    private String 지원금산정;
}
