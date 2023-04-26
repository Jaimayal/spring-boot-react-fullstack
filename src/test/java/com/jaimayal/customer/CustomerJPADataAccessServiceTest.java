package com.jaimayal.customer;

import com.jaimayal.CustomEntityFaker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Random;

import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {
    
    private CustomerJPADataAccessService underTest;
    @Mock 
    private CustomerRepository customerRepository;
    private AutoCloseable autoCloseable;
    private static final CustomEntityFaker ENTITY_FAKER = new CustomEntityFaker();
    
    @BeforeEach
    void setUp() {
        this.autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        this.autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        // When
        underTest.selectAllCustomers();
        
        // Then
        verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        // Given
        Long id = new Random().nextLong(50000, 300000);
        
        // When
        underTest.selectCustomerById(id);
        
        // Then
        verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        // Given
        Customer customer = ENTITY_FAKER.getCustomer();
                
        // When
        underTest.insertCustomer(customer);
        
        // Then
        verify(customerRepository).save(customer);
    }

    @Test
    void existsCustomerById() {
        // Given
        Long id = new Random().nextLong(50000, 300000);
        
        // When
        underTest.existsCustomerById(id);

        // Then
        verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void existsCustomerByEmail() {
        // Given
        String email = ENTITY_FAKER.getCustomer().getEmail();

        // When
        underTest.existsCustomerByEmail(email);

        // Then
        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void updateCustomer() {
        // Given
        Customer customer = ENTITY_FAKER.getCustomer();

        // When
        underTest.updateCustomer(customer);

        // Then
        verify(customerRepository).save(customer);
    }

    @Test
    void deleteCustomerById() {
        // Given
        Long id = new Random().nextLong(50000, 300000);

        // When
        underTest.deleteCustomerById(id);

        // Then
        verify(customerRepository).deleteById(id);
    }
}