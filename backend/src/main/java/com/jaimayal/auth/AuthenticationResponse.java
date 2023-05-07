package com.jaimayal.auth;

import com.jaimayal.customer.CustomerDTO;

public record AuthenticationResponse (
        String token,
        CustomerDTO customer
) {}
