package com.jaimayal.customer;

import com.jaimayal.security.JwtUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
    
    private final CustomerService customerService;
    private final JwtUtils jwtUtils;
    
    public CustomerController(CustomerService customerService, 
                              JwtUtils jwtUtils) {
        this.customerService = customerService;
        this.jwtUtils = jwtUtils;
    }
    
    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }
    
    @GetMapping("/{customerId}")
    public Customer getCustomer(@PathVariable Long customerId) {
        return customerService.getCustomer(customerId);
    }
    
    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        customerService.createCustomer(customerRegistrationRequest);
        String token = jwtUtils.issueToken(customerRegistrationRequest.email(), "ROLE_USER");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
    }
    
    @PutMapping("/{customerId}")
    public void updateCustomer(@PathVariable Long customerId,
                               @RequestBody Customer customer) {
        customerService.updateCustomer(customerId, customer);
    }
    
    @DeleteMapping("/{customerId}")
    public void deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
    }
}
