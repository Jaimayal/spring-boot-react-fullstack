package com.jaimayal.customer;

import com.jaimayal.exception.DuplicatedResourceException;
import com.jaimayal.exception.InvalidResourceException;
import com.jaimayal.exception.InvalidResourceUpdatesException;
import com.jaimayal.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    
    private final CustomerDao customerDao;
    private final PasswordEncoder passwordEncoder;
    private final CustomerDTOMapper customerDTOMapper;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao,
                           PasswordEncoder passwordEncoder, 
                           CustomerDTOMapper customerDTOMapper) {
        this.customerDao = customerDao;
        this.passwordEncoder = passwordEncoder;
        this.customerDTOMapper = customerDTOMapper;
    }

    public List<CustomerDTO> getCustomers() {
        return customerDao.selectAllCustomers().stream()
                .map(customerDTOMapper)
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomer(Long customerId) {
        return customerDao.selectCustomerById(customerId)
                .map(customerDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with ID [" + customerId + "] not found"
                ));
    }

    public void createCustomer(CustomerRegistrationRequest customer) {
        this.checkIfIsEmailTaken(customer.email());
        this.checkIfGenderIsValid(customer.gender());
        customerDao.insertCustomer(new Customer(
                customer.name(),
                customer.email(),
                passwordEncoder.encode(customer.password()),
                customer.age(),
                customer.gender()));
    }

    public void updateCustomer(Long customerId, CustomerUpdateDTO update) {
        Customer customerToUpdate = customerDao.selectCustomerById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with ID [" + customerId + "] not found"
                ));
        
        boolean updates = false;
        if (update.name() != null && !update.name().equals(customerToUpdate.getName())) {
            customerToUpdate.setName(update.name());
            updates = true;
        }
        
        if (update.age() != null && !update.age().equals(customerToUpdate.getAge())) {
            customerToUpdate.setAge(update.age());
            updates = true;
        }

        if (update.email() != null && !update.email().equals(customerToUpdate.getEmail())) {
            this.checkIfIsEmailTaken(update.email());
            customerToUpdate.setEmail(update.email());
            updates = true;
        }
        
        if (update.gender() != null && !update.gender().equals(customerToUpdate.getGender())) {
            this.checkIfGenderIsValid(update.gender());
            customerToUpdate.setGender(update.gender());
            updates = true;
        }
        
        if (update.password() != null && !passwordEncoder.matches(update.password(), customerToUpdate.getPassword())) {
            customerToUpdate.setPassword(passwordEncoder.encode(update.password()));
            updates = true;
        }
        
        if (!updates) {
            throw new InvalidResourceUpdatesException(
                    "No updates were provided for customer with ID [" + customerId + "]"
            );
        }
        
        customerDao.updateCustomer(customerToUpdate);
    }

    private void checkIfGenderIsValid(String gender) {
        if (!"male".equals(gender) && !"female".equals(gender)) {
            throw new InvalidResourceException(
                    "The provided gender [" + gender + "] is invalid"
            );
        }

    }

    private void checkIfIsEmailTaken(String email) {
        if (customerDao.existsCustomerByEmail(email)) {
            throw new DuplicatedResourceException(
                    "Customer with email [" + email + "] already exists"
            );
        }
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
