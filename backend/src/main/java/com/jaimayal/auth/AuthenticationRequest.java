package com.jaimayal.auth;

public record AuthenticationRequest (
        String email, 
        String password
) {}
