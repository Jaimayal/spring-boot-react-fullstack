package com.jaimayal.customer;

import com.jaimayal.CustomEntityFaker;
import com.jaimayal.exception.DuplicatedResourceException;
import com.jaimayal.exception.InvalidResourceException;
import com.jaimayal.exception.InvalidResourceUpdatesException;
import com.jaimayal.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private static final CustomEntityFaker ENTITY_FAKER = new CustomEntityFaker();
    
    private CustomerService underTest;
    @Mock
    private CustomerDao customerDao;
    @Mock
    private PasswordEncoder passwordEncoder;
    private CustomerDTOMapper customerDTOMapper;

    @BeforeEach
    void setUp() {
        this.customerDTOMapper = new CustomerDTOMapper();
        this.underTest = new CustomerService(customerDao, passwordEncoder, this.customerDTOMapper);
    }

    @Test
    void verifyGetCustomersCallsDaoSelectAllMethod() {
        // When
        underTest.getCustomers();
        
        // Then
        verify(customerDao).selectAllCustomers();
    }
    
    @Test
    void verifyGetCustomerCallsDaoSelectByIdMethod() {
        // Given
        Long id = ENTITY_FAKER.getId();
        Customer customer = ENTITY_FAKER.getCustomer();
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        // When
        underTest.getCustomer(id);

        // Then
        verify(customerDao).selectCustomerById(id);
    }

    @Test
    void checkGetCustomerReturnsCustomer() {
        // Given
        Long id = ENTITY_FAKER.getId();
        Customer customer = ENTITY_FAKER.getCustomer();
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerDTO expected = this.customerDTOMapper.apply(customer);
        
        // When
        CustomerDTO actual = underTest.getCustomer(id);

        // Then
        assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    void checkGetCustomerThrowsResourceNotFoundExceptionIfCustomerIsNotFound() {
        // Given
        Long id = ENTITY_FAKER.getId();
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with ID [" + id + "] not found");
    }
    
    @Test
    void checkCreateCustomerIsSuccessful() {
        // Given
        Customer fakeCustomer = ENTITY_FAKER.getCustomer();
        CustomerRegistrationRequest registration = new CustomerRegistrationRequest(
                fakeCustomer.getName(),
                fakeCustomer.getEmail(),
                "password", 
                fakeCustomer.getAge(),
                fakeCustomer.getGender()
        );
        
        String hashedPassword = UUID.randomUUID().toString();
        when(customerDao.existsCustomerByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn(hashedPassword);
        // When
        underTest.createCustomer(registration);
        
        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        
        assertThat(capturedCustomer.getName()).isEqualTo(registration.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(registration.email());
        assertThat(capturedCustomer.getPassword()).isEqualTo(hashedPassword);
        assertThat(capturedCustomer.getAge()).isEqualTo(registration.age());
    }
    
    @Test
    void checkCreateCustomerThrowsDuplicateResourceExceptionIfCustomerAlreadyExists() {
        // Given
        Customer fakeCustomer = ENTITY_FAKER.getCustomer();
        CustomerRegistrationRequest registration = new CustomerRegistrationRequest(
                fakeCustomer.getName(),
                fakeCustomer.getEmail(),
                "password", fakeCustomer.getAge(),
                fakeCustomer.getGender()
        );
        when(customerDao.existsCustomerByEmail(anyString())).thenReturn(true);

        // When
        assertThatThrownBy(() -> underTest.createCustomer(registration))
                .isInstanceOf(DuplicatedResourceException.class)
                .hasMessage("Customer with email [" + registration.email() + "] already exists");
        
        // Then
        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void checkUpdateCustomerUpdatesAllCustomerFieldsSuccessfully() {
        // Given
        Customer previous = ENTITY_FAKER.getCustomer();
        Customer fakedUpdated = ENTITY_FAKER.getCustomer();
        String updatedGender = previous.getGender().equals("male") ? "female" : "male";
        CustomerUpdateDTO updated = new CustomerUpdateDTO(
                fakedUpdated.getName(),
                fakedUpdated.getEmail(),
                fakedUpdated.getPassword(),
                fakedUpdated.getAge(),
                updatedGender
        );
        
        
        Long previousId = ENTITY_FAKER.getId();
        
        when(customerDao.selectCustomerById(previousId))
                .thenReturn(Optional.of(previous));
        when(customerDao.existsCustomerByEmail(updated.email()))
                .thenReturn(false);
        when(passwordEncoder.matches(any(), any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("password");
        
        // When
        underTest.updateCustomer(previousId, updated);
        
        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer customerCaptured = customerArgumentCaptor.getValue();
        
        assertThat(customerCaptured.getName()).isEqualTo(updated.name());
        assertThat(customerCaptured.getEmail()).isEqualTo(updated.email());
        assertThat(customerCaptured.getPassword()).isEqualTo(updated.password());
        assertThat(customerCaptured.getAge()).isEqualTo(updated.age());
        assertThat(customerCaptured.getGender()).isEqualTo(updated.gender());
    }

    @Test
    void checkUpdateCustomerUpdatesNameSuccessfully() {
        // Given
        Customer previous = ENTITY_FAKER.getCustomer();
        String updatedName = previous.getName() + "UPDATED";
        CustomerUpdateDTO updated = new CustomerUpdateDTO(
                updatedName,
                previous.getEmail(),
                previous.getPassword(), 
                previous.getAge(),
                previous.getGender());
        Long previousId = ENTITY_FAKER.getId();

        when(customerDao.selectCustomerById(previousId))
                .thenReturn(Optional.of(previous));

        // When
        underTest.updateCustomer(previousId, updated);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer customerCaptured = customerArgumentCaptor.getValue();

        assertThat(customerCaptured.getName()).isEqualTo(updated.name());
        assertThat(customerCaptured.getEmail()).isEqualTo(previous.getEmail());
        assertThat(customerCaptured.getAge()).isEqualTo(previous.getAge());
        assertThat(customerCaptured.getGender()).isEqualTo(previous.getGender());
    }

    @Test
    void checkUpdateCustomerUpdatesEmailSuccessfully() {
        // Given
        Customer previous = ENTITY_FAKER.getCustomer();
        String updatedEmail = previous.getEmail() + "UPDATED";
        CustomerUpdateDTO updated = new CustomerUpdateDTO(
                previous.getName(), 
                updatedEmail,
                previous.getPassword(), 
                previous.getAge(),
                previous.getGender()
        );
        Long previousId = ENTITY_FAKER.getId();

        when(customerDao.selectCustomerById(previousId))
                .thenReturn(Optional.of(previous));
        when(customerDao.existsCustomerByEmail(updatedEmail))
                .thenReturn(false);

        // When
        underTest.updateCustomer(previousId, updated);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer customerCaptured = customerArgumentCaptor.getValue();

        assertThat(customerCaptured.getName()).isEqualTo(previous.getName());
        assertThat(customerCaptured.getEmail()).isEqualTo(updatedEmail);
        assertThat(customerCaptured.getAge()).isEqualTo(previous.getAge());
        assertThat(customerCaptured.getGender()).isEqualTo(previous.getGender());
    }

    @Test
    void checkUpdateCustomerUpdatesAgeSuccessfully() {
        // Given
        Customer previous = ENTITY_FAKER.getCustomer();
        Integer updatedAge = previous.getAge() + 1;
        CustomerUpdateDTO updated = new CustomerUpdateDTO(
                previous.getName(),
                previous.getEmail(),
                previous.getPassword(), 
                updatedAge,
                previous.getGender()
        );
        Long previousId = ENTITY_FAKER.getId();

        when(customerDao.selectCustomerById(previousId))
                .thenReturn(Optional.of(previous));

        // When
        underTest.updateCustomer(previousId, updated);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer customerCaptured = customerArgumentCaptor.getValue();

        assertThat(customerCaptured.getName()).isEqualTo(previous.getName());
        assertThat(customerCaptured.getEmail()).isEqualTo(previous.getEmail());
        assertThat(customerCaptured.getAge()).isEqualTo(updatedAge);
        assertThat(customerCaptured.getGender()).isEqualTo(previous.getGender());
    }

    @Test
    void checkUpdateCustomerUpdatesGenderSuccessfully() {
        // Given
        Customer previous = ENTITY_FAKER.getCustomer();
        String updatedGender = previous.getGender().equals("male") ? "female" : "male";
        CustomerUpdateDTO updated = new CustomerUpdateDTO(
                previous.getName(),
                previous.getEmail(),
                previous.getPassword(),
                previous.getAge(), 
                updatedGender);
        Long previousId = ENTITY_FAKER.getId();

        when(customerDao.selectCustomerById(previousId))
                .thenReturn(Optional.of(previous));

        // When
        underTest.updateCustomer(previousId, updated);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer customerCaptured = customerArgumentCaptor.getValue();

        assertThat(customerCaptured.getName()).isEqualTo(previous.getName());
        assertThat(customerCaptured.getEmail()).isEqualTo(previous.getEmail());
        assertThat(customerCaptured.getAge()).isEqualTo(previous.getAge());
        assertThat(customerCaptured.getGender()).isEqualTo(updatedGender);
    }

    @Test
    void checkUpdateCustomerThrowsResourceNotFoundExceptionIfCustomerIsNotFound() {
        // Given
        Customer fakedCustomer = ENTITY_FAKER.getCustomer();
        CustomerUpdateDTO updated = new CustomerUpdateDTO(
                fakedCustomer.getName(),
                fakedCustomer.getEmail(),
                fakedCustomer.getPassword(),
                fakedCustomer.getAge(),
                fakedCustomer.getGender()
        );
        Long nonexistentCustomerId = ENTITY_FAKER.getId();
        when(customerDao.selectCustomerById(nonexistentCustomerId))
                .thenReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> underTest.updateCustomer(nonexistentCustomerId, updated))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with ID [" + nonexistentCustomerId + "] not found");

        // Then
        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void checkUpdateCustomerThrowsDuplicatedResourceExceptionIfEmailIsTaken() {
        // Given
        Customer previous = ENTITY_FAKER.getCustomer();
        String updatedEmail = previous.getEmail() + "UPDATED";
        CustomerUpdateDTO updated = new CustomerUpdateDTO(
                previous.getName(),
                updatedEmail,
                previous.getPassword(), 
                previous.getAge(),
                previous.getGender()
        );
        Long previousId = ENTITY_FAKER.getId();

        when(customerDao.selectCustomerById(previousId))
                .thenReturn(Optional.of(previous));
        when(customerDao.existsCustomerByEmail(updatedEmail))
                .thenReturn(true);

        // When
        assertThatThrownBy(() -> underTest.updateCustomer(previousId, updated))
                .isInstanceOf(DuplicatedResourceException.class)
                .hasMessage("Customer with email [" + updatedEmail + "] already exists");

        // Then
        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void checkUpdateCustomerThrowsInvalidResourceUpdatesExceptionIfNoUpdatedRequired() {
        // Given
        Customer previous = ENTITY_FAKER.getCustomer();
        CustomerUpdateDTO updated = new CustomerUpdateDTO(
                previous.getName(),
                previous.getEmail(),
                previous.getPassword(),
                previous.getAge(),
                previous.getGender()
        );
        Long previousId = ENTITY_FAKER.getId();

        when(customerDao.selectCustomerById(previousId))
                .thenReturn(Optional.of(previous));
        when(passwordEncoder.matches(any(), any()))
                .thenReturn(true);
        
        // When
        assertThatThrownBy(() -> underTest.updateCustomer(previousId, updated))
                .isInstanceOf(InvalidResourceUpdatesException.class)
                .hasMessage("No updates were provided for customer with ID [" + previousId + "]");

        // Then
        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void checkUpdateCustomerThrowsInvalidResourceExceptionIfNoValidGenderIsProvided() {
        // Given
        Customer previous = ENTITY_FAKER.getCustomer();
        String invalidGender = "INVALID";
        CustomerUpdateDTO updated = new CustomerUpdateDTO(
                previous.getName(),
                previous.getEmail(),
                previous.getPassword(),
                previous.getAge(),
                invalidGender);
        Long previousId = ENTITY_FAKER.getId();

        when(customerDao.selectCustomerById(previousId))
                .thenReturn(Optional.of(previous));

        // When
        assertThatThrownBy(() -> underTest.updateCustomer(previousId, updated))
                .isInstanceOf(InvalidResourceException.class)
                .hasMessage("The provided gender [" + invalidGender + "] is invalid");

        // Then
        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void verifyDeleteCustomerCallsDaoDeleteByIdMethod() {
        // Given
        Long id = ENTITY_FAKER.getId();
        when(customerDao.existsCustomerById(id)).thenReturn(true);
        
        // When
        underTest.deleteCustomer(id);

        // Then
        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void checkDeleteCustomerThrowsResourceNotFoundExceptionIfCustomerIsNotFound() {
        // Given
        Long id = ENTITY_FAKER.getId();
        when(customerDao.existsCustomerById(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.deleteCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with ID [" + id + "] not found");

        // Then
        verify(customerDao, never()).deleteCustomerById(id);
    }
}