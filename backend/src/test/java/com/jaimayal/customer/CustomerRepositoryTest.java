package com.jaimayal.customer;

import com.jaimayal.AbstractDaoTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractDaoTest {
    
    @Autowired
    private CustomerRepository underTest;

    @Test
    void givenSavedCustomerEmailThenCustomerExistenceIsTrue() {
        // Given
        Customer saved = ENTITY_FAKER.getCustomer();
        underTest.save(saved);

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
    void givenSavedCustomerIdThenCustomerExistenceIsTrue() {
        // Given
        Customer saved = ENTITY_FAKER.getCustomer();
        underTest.save(saved);
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

    private Long getCustomerIdInDatabase(Customer customer) {
        return underTest.findAll().stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
    }
}