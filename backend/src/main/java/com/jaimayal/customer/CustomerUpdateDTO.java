package com.jaimayal.customer;

public record CustomerUpdateDTO (
        String name,
        String email,
        String password,
        Integer age, 
        String gender
) {}
