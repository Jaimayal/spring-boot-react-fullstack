package com.jaimayal.customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomerJPADataAccessService implements CustomerDao {
    
    private final CustomerRepository customerRepository;

    public CustomerJPADataAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> selectCustomerById(Long customerId) {
        return customerRepository.findById(customerId);
    }

    @Override
    public void insertCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public boolean existsCustomerById(Long customerId) {
        return customerRepository.existsCustomerById(customerId);
    }

    @Override
    public boolean existsCustomerByEmail(String email) {
        return customerRepository.existsCustomerByEmail(email);
    }

    @Override
    public void updateCustomer(Customer update) {
        customerRepository.save(update);
    }

    @Override
    public void deleteCustomerById(Long customerId) {
        customerRepository.deleteById(customerId);
    }
}
