package com.jaimayal.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age
) {}
