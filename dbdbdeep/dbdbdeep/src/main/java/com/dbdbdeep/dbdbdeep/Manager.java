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
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mg_id;

    @Column(length = 20)
    private String mg_name;

    @Column(length = 20)
    private String mg_phone;

    @Column(length = 50)
    private String mg_mail;
}
