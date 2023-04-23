package com.jaimayal.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Customer {
    
    @Id
    @SequenceGenerator(
            name = "customer_id_seq", 
            sequenceName = "customer_id_seq", 
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "customer_id_seq", 
            strategy = GenerationType.SEQUENCE
    )
    private Long id;
    
    @Column(
            nullable = false
    )
    private String name;
    
    @Column(
            nullable = false
    )
    private String email;
    
    @Column(
            nullable = false
    )
    private Integer age;

    public Customer(Long id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }
    
    public Customer(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public Customer() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
