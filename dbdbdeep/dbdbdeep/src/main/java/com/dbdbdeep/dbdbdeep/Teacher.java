package com.dbdbdeep.dbdbdeep;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tc_id;

    @Column(length = 20)
    private String tc_name;

    @Column(length = 20)
    private String tc_phone;

    @Column(length = 50)
    private String tc_email;
}