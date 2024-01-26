package com.dbdbdeep.dbdbdeep;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer std_id;

    @Column(length = 20)
    private String std_name;

    @Column(length = 20)
    private String std_phone;

    @Column(length = 40)
    private String std_mail;

    @Column
    private  Integer gen_id;

    @Column
    private  Integer mt_id;

    @Column
    private String std_vol;
}

