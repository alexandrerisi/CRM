package com.risi.mvc.data.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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
