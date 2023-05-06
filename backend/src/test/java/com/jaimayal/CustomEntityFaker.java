package com.jaimayal;

import com.github.javafaker.Faker;
import com.jaimayal.customer.Customer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;
import java.util.UUID;

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
        String password = "password";
        Integer age = faker.random().nextInt(18,50);
        String gender = faker.random().nextInt(5) % 2 == 0 ? "male" : "female";
        
        return new Customer(
                firstName,
                email,
                password, 
                age,
                gender);
    }
    
    public Long getId() {
        return new Random().nextLong(50000, 300000);
    }
}
