package com.jaimayal.utils;

import com.github.javafaker.Faker;
import com.jaimayal.customer.Customer;
import com.jaimayal.customer.CustomerDTO;
import com.jaimayal.customer.CustomerRegistrationRequest;
import com.jaimayal.customer.CustomerUpdateDTO;

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
    
    public CustomerDTO getCustomerDTO() {
        Customer customer = getCustomer();
        return new CustomerDTO(
                null,
                customer.getEmail(),
                customer.getPassword(),
                customer.getAge(),
                customer.getGender()
        );
    }
    
    public CustomerUpdateDTO getCustomerUpdateDTO(CustomerDTO previous) {
        Customer customer = getCustomer();
        Integer updatedAge = previous.age() + 1;
        String updatedGender = "male".equals(previous.gender()) ? "female" : "male";
        return new CustomerUpdateDTO(
                customer.getName(),
                customer.getEmail(),
                customer.getPassword(),
                updatedAge,
                updatedGender
        );
    }
    
    public CustomerRegistrationRequest getCustomerRegistrationRequest() {
        Customer customer = getCustomer();
        return new CustomerRegistrationRequest(
                customer.getName(),
                customer.getEmail(),
                customer.getPassword(),
                customer.getAge(),
                customer.getGender()
        );
    }
    
    public Long getId() {
        return new Random().nextLong(50000, 300000);
    }
}
