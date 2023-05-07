package com.jaimayal.customer;

public record CustomerDTO (
        Long id,
        String name,
        String email,
        Integer age,
        String gender
) {}
