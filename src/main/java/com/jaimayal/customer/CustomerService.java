package com.jaimayal.customer;

import com.jaimayal.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CustomerService {
    
    private final CustomerDao customerDao;

    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Long customerId) {
        return customerDao.selectCustomerById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with ID [" + customerId + "] not found"
                ));
    }

    public void createCustomer(CustomerRegistrationRequest customer) {
        customerDao.insertCustomer(new Customer(
                customer.name(),
                customer.email(),
                customer.age()
        ));
    }

    public void updateCustomer(Long customerId, Customer update) {
        Customer customerToUpdate = this.getCustomer(customerId);
        
        boolean updates = false;
        if (update.getName() != null && !update.getName().equals(customerToUpdate.getName())) {
            customerToUpdate.setName(update.getName());
            updates = true;
        }
        
        if (update.getAge() != null && !update.getAge().equals(customerToUpdate.getAge())) {
            customerToUpdate.setAge(update.getAge());
            updates = true;
        }

        if (update.getEmail() != null && !update.getEmail().equals(customerToUpdate.getEmail())) {
            if (customerDao.existsCustomerByEmail(update.getEmail())) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Email [" + update.getEmail() + "] already exists"
                );
            }
            customerToUpdate.setEmail(update.getEmail());
            updates = true;
        }
        
        if (!updates) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No updates were provided for customer with ID [" + customerId + "]"
            );
        }
        
        customerDao.updateCustomer(customerToUpdate);
    }

    public void deleteCustomer(Long customerId) {
        if (!customerDao.existsCustomerById(customerId)) {
            throw new ResourceNotFoundException(
                    "Customer with ID [" + customerId + "] not found"
            );
        }
        
        customerDao.deleteCustomerById(customerId);
    }
}
