package com.jaimayal.customer;

import com.jaimayal.CustomEntityFaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerRowMapperTest {
    
    private static final CustomEntityFaker ENTITY_FAKER = new CustomEntityFaker();
    
    private CustomerRowMapper underTest;
    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        underTest = new CustomerRowMapper();
    }

    @Test
    void mapRow() throws SQLException {
        // Given
        Long id = ENTITY_FAKER.getId();
        Customer expected = ENTITY_FAKER.getCustomer();
        expected.setId(id);
        
        when(resultSet.getLong("id")).thenReturn(expected.getId());
        when(resultSet.getString("name")).thenReturn(expected.getName());
        when(resultSet.getString("email")).thenReturn(expected.getEmail());
        when(resultSet.getInt("age")).thenReturn(expected.getAge());
        when(resultSet.getString("gender")).thenReturn(expected.getGender());
        
        // When
        Customer actual = underTest.mapRow(resultSet, 1);

        // Then
        assertThat(actual).isEqualTo(expected);
    }
}