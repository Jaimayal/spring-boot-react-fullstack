package com.jaimayal.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Long customerId);
    void insertCustomer(Customer customer);
    boolean existsCustomerById(Long customerId);
    boolean existsCustomerByEmail(String email);
    void updateCustomer(Customer update);
    void deleteCustomerById(Long customerId);
    Optional<Customer> selectCustomerByEmail(String email);
}
