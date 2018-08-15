package com.risi.mvc.data.demo.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@ToString
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue
    private int id;
    private String firstName;
    private String surName;
    private String email;

    public Customer(String firstName, String surName, String email) {
        this.firstName = firstName;
        this.surName = surName;
        this.email = email;
    }
}
