package com.jaimayal;

import com.github.javafaker.Faker;
import com.jaimayal.customer.Customer;

import java.util.Random;

public class CustomEntityFaker {
    private final Faker faker;


    public CustomEntityFaker() {
        this.faker = new Faker();
    }
    
    public Customer getCustomer() {
        String fullName = faker.name().fullName();
        String firstName = fullName.split(" ")[0].toLowerCase();
        String lastName = fullName.split(" ")[1].toLowerCase();
        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@gmail.com";
        Integer age = faker.random().nextInt(18,50);
        
        return new Customer(
                firstName,
                email,
                age
        );
    }
    
    public Long getId() {
        return new Random().nextLong(50000, 300000);
    }
}
