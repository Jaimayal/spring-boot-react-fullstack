package com.jaimayal.customer;

import com.jaimayal.AbstractDaoTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractDaoTest {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper rowMapper = new CustomerRowMapper();
    
    @BeforeEach
    void setUp() {
        this.underTest = new CustomerJDBCDataAccessService(
                new JdbcTemplate(getDataSource()), 
                rowMapper
        );
    }


    @Test
    void selectAllCustomers() {
        // Given
        Customer customer = ENTITY_FAKER.getCustomer();
        underTest.insertCustomer(customer);
        
        // When
        List<Customer> customers = underTest.selectAllCustomers();
        
        // Then
        assertThat(customers).isNotEmpty();
    }

    private Long getCustomerIdInDatabase(Customer customer) {
        return underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
    }

    @Test
    void selectCustomerById() {
        // Given
        Customer customer = ENTITY_FAKER.getCustomer();
        underTest.insertCustomer(customer);
        Long id = this.getCustomerIdInDatabase(customer);
        
        // When
        Optional<Customer> actualOptional = underTest.selectCustomerById(id);

        // Then
        assertThat(actualOptional).isPresent().hasValueSatisfying(actual -> {
            assertThat(actual.getId()).isEqualTo(id);
            assertThat(actual.getEmail()).isEqualTo(customer.getEmail());
            assertThat(actual.getName()).isEqualTo(customer.getName());
            assertThat(actual.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void insertCustomer() {
        // Given
        Customer customer = ENTITY_FAKER.getCustomer();
        List<Customer> oldList = underTest.selectAllCustomers();
        
        // When
        underTest.insertCustomer(customer);
        List<Customer> actualList = underTest.selectAllCustomers();
        
        // Then
        assertThat(actualList.size()).isGreaterThan(oldList.size());
    }

    @Test
    void givenSavedCustomerIdThenCustomerExistenceIsTrue() {
        // Given
        Customer saved = ENTITY_FAKER.getCustomer();
        underTest.insertCustomer(saved);
        Long savedId = this.getCustomerIdInDatabase(saved);

        // When
        boolean exists = underTest.existsCustomerById(savedId);
        
        // Then
        assertThat(exists).isTrue();
    }
    
    @Test
    void givenNotSavedCustomerIdThenCustomerExistenceIsFalse() {
        // Given
        Long notSavedId = 0L;

        // When
        boolean exists = underTest.existsCustomerById(notSavedId);

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void givenSavedCustomerEmailThenCustomerExistenceIsTrue() {
        // Given
        Customer saved = ENTITY_FAKER.getCustomer();
        underTest.insertCustomer(saved);

        // When
        boolean exists = underTest.existsCustomerByEmail(saved.getEmail());

        // Then
        assertThat(exists).isTrue();
    }
    
    @Test
    void givenNotSavedCustomerEmailThenCustomerExistenceIsFalse() {
        // Given
        Customer notSaved = ENTITY_FAKER.getCustomer();

        // When
        boolean exists = underTest.existsCustomerByEmail(notSaved.getEmail());

        // Then
        assertThat(exists).isFalse();
    }
    
    @Test
    void updateNameFieldOnCustomer() {
        // Given
        Customer old = ENTITY_FAKER.getCustomer();
        String updatedName = "NAME";
        underTest.insertCustomer(old);

        Long id = this.getCustomerIdInDatabase(old);
        old.setId(id);

        // When
        old.setName(updatedName);
        underTest.updateCustomer(old);

        // Then
        Optional<Customer> actualOptional = underTest.selectCustomerById(id);
        
        assertThat(actualOptional).isPresent().hasValueSatisfying(actual -> {
            assertThat(actual.getEmail()).isEqualTo(old.getEmail());
            assertThat(actual.getName()).isEqualTo(updatedName);
            assertThat(actual.getAge()).isEqualTo(old.getAge());
        });
    }

    @Test
    void updateEmailFieldOnCustomer() {
        // Given
        Customer old = ENTITY_FAKER.getCustomer();
        String updatedEmail = "name.@gmail.com";
        underTest.insertCustomer(old);

        Long id = this.getCustomerIdInDatabase(old);
        old.setId(id);

        // When
        old.setEmail(updatedEmail);
        underTest.updateCustomer(old);

        // Then
        Optional<Customer> actualOptional = underTest.selectCustomerById(id);

        assertThat(actualOptional).isPresent().hasValueSatisfying(actual -> {
            assertThat(actual.getEmail()).isEqualTo(updatedEmail);
            assertThat(actual.getName()).isEqualTo(old.getName());
            assertThat(actual.getAge()).isEqualTo(old.getAge());
        });
    }

    @Test
    void updateAgeFieldOnCustomer() {
        // Given
        Customer old = ENTITY_FAKER.getCustomer();
        Integer updatedAge = 123;
        underTest.insertCustomer(old);

        Long id = this.getCustomerIdInDatabase(old);
        old.setId(id);

        // When
        old.setAge(updatedAge);
        underTest.updateCustomer(old);

        // Then
        Optional<Customer> actualOptional = underTest.selectCustomerById(id);

        assertThat(actualOptional).isPresent().hasValueSatisfying(actual -> {
            assertThat(actual.getEmail()).isEqualTo(old.getEmail());
            assertThat(actual.getName()).isEqualTo(old.getName());
            assertThat(actual.getAge()).isEqualTo(updatedAge);
        });
    }

    @Test
    void updateAllCustomerFields() {
        // Given
        Customer old = ENTITY_FAKER.getCustomer();
        Customer updated = ENTITY_FAKER.getCustomer();
        underTest.insertCustomer(old);
        Long id = this.getCustomerIdInDatabase(old);
        old.setId(id);
        
        // When
        old.setName(updated.getName());
        old.setEmail(updated.getEmail());
        old.setAge(updated.getAge());
        underTest.updateCustomer(old);

        // Then
        Optional<Customer> actualOptional = underTest.selectCustomerById(id);
        assertThat(actualOptional).isPresent().hasValueSatisfying(actual -> {
            assertThat(actual.getEmail()).isEqualTo(updated.getEmail());
            assertThat(actual.getName()).isEqualTo(updated.getName());
            assertThat(actual.getAge()).isEqualTo(updated.getAge());
        });
    }
    
    @Test
    void updateDoesNotApplyIfThereIsNoChanges() {
        // Given
        Customer old = ENTITY_FAKER.getCustomer();
        underTest.insertCustomer(old);
        Long id = this.getCustomerIdInDatabase(old);

        // When
        old.setId(id);
        underTest.updateCustomer(old);

        // Then
        Optional<Customer> actualOptional = underTest.selectCustomerById(id);
        
        assertThat(actualOptional).isPresent().hasValueSatisfying(actual -> {
            assertThat(actual.getEmail()).isEqualTo(old.getEmail());
            assertThat(actual.getName()).isEqualTo(old.getName());
            assertThat(actual.getAge()).isEqualTo(old.getAge());
        });
    }

    @Test
    void deleteCustomerById() {
        // Given
        Customer customer = ENTITY_FAKER.getCustomer();
        underTest.insertCustomer(customer);
        Long id = this.getCustomerIdInDatabase(customer);
        
        // When
        underTest.deleteCustomerById(id);
        Optional<Customer> actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isEmpty();
    }
    
    
}