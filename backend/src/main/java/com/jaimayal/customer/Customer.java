package com.jaimayal.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

import java.util.Objects;

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
    
    @Column(
            columnDefinition = "gender TEXT CHECK (gender IN ('male', 'female'));",
            nullable = false
    )
    private String gender;

    public Customer(Long id, String name, String email, Integer age, String gender) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
    }
    
    public Customer(String name, String email, Integer age, String gender) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
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
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) && Objects.equals(name, customer.name) && Objects.equals(email, customer.email) && Objects.equals(age, customer.age) && Objects.equals(gender, customer.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, age, gender);
    }
}
