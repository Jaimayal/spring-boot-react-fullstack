package com.jaimayal.auth;

import com.jaimayal.config.JwtService;
import com.jaimayal.customer.Customer;
import com.jaimayal.customer.CustomerDTO;
import com.jaimayal.customer.CustomerDTOMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    
    private final AuthenticationManager authenticationManager;
    private final CustomerDTOMapper customerDTOMapper;
    private final JwtService jwtService;
    
    public AuthenticationService(AuthenticationManager authenticationManager,
                                 CustomerDTOMapper customerDTOMapper, 
                                 JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.customerDTOMapper = customerDTOMapper;
        this.jwtService = jwtService;
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        
        Customer principal = (Customer) authentication.getPrincipal();
        CustomerDTO dto = customerDTOMapper.apply(principal);
        String token = jwtService.issueToken(dto.email(), "ROLE_USER");
        return new AuthenticationResponse(token);
    }
}
