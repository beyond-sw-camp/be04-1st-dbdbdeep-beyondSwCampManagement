package com.dbdbdeep.dbdbdeep;


import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Rollbook {

    @Id
    private Date    rb_date;

    @Id
    private Integer std_id;

    private String status;
}
