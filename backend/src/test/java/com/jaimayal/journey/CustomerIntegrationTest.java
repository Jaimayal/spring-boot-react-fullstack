package com.jaimayal.journey;

import com.jaimayal.CustomEntityFaker;
import com.jaimayal.customer.Customer;
import com.jaimayal.customer.CustomerDTO;
import com.jaimayal.customer.CustomerRegistrationRequest;
import com.jaimayal.customer.CustomerUpdateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {
    
    private static final CustomEntityFaker ENTITY_FAKER = new CustomEntityFaker();
    private static final String API_URI = "api/v1/customers";
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void canRegisterCustomer() {
        // Create a CustomerRegistrationRequest
        Customer fakedCustomer = ENTITY_FAKER.getCustomer();
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                fakedCustomer.getName(),
                fakedCustomer.getEmail(),
                "password", 
                fakedCustomer.getAge(),
                fakedCustomer.getGender()
        );
        
        // Send a POST request to "api/v1/customers"
        HttpHeaders httpHeaders = webTestClient.post()
                .uri(API_URI)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders();

        // Extract JWT Token
        String token = httpHeaders.getFirst(HttpHeaders.AUTHORIZATION);
        
        // GET all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(API_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {})
                .returnResult()
                .getResponseBody();

        // Make sure that Customer is present
        assertThat(allCustomers).isNotNull();
        
        // GET customer by Id
        Long id = allCustomers.stream()
                .filter(c -> c.email().equals(fakedCustomer.getEmail()))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();
        
        CustomerDTO expected = new CustomerDTO(
                id,
                fakedCustomer.getName(),
                fakedCustomer.getEmail(),
                fakedCustomer.getAge(),
                fakedCustomer.getGender()
        );
        
        assertThat(allCustomers).contains(expected);
        
        webTestClient.get()
                .uri(API_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .isEqualTo(expected);
    }

    @Test
    void canDeleteCustomer() {
        // Create a CustomerRegistrationRequest
        Customer fakedCustomer = ENTITY_FAKER.getCustomer();
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                fakedCustomer.getName(),
                fakedCustomer.getEmail(),
                "password",
                fakedCustomer.getAge(),
                fakedCustomer.getGender()
        );

        // Send a POST request to "api/v1/customers"
        HttpHeaders httpHeaders = webTestClient.post()
                .uri(API_URI)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders();

        // Extract JWT Token
        String token = httpHeaders.getFirst(HttpHeaders.AUTHORIZATION);

        // GET all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(API_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        // Make sure that Customer is present
        assertThat(allCustomers).isNotNull();

        // GET customer by Id
        Long id = allCustomers.stream()
                .filter(c -> c.email().equals(fakedCustomer.getEmail()))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();

        CustomerDTO expected = new CustomerDTO(
                id,
                fakedCustomer.getName(),
                fakedCustomer.getEmail(),
                fakedCustomer.getAge(),
                fakedCustomer.getGender()
        );

        assertThat(allCustomers).contains(expected);
        
        // DELETE customer by Id
        webTestClient.delete()
                .uri(API_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchange()
                .expectStatus()
                .isNoContent();
        
        // Check customer is NOT FOUND
        webTestClient.get()
                .uri(API_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomer() {
        // Create a CustomerRegistrationRequest
        Customer fakedCustomer = ENTITY_FAKER.getCustomer();
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                fakedCustomer.getName(),
                fakedCustomer.getEmail(),
                "password",
                fakedCustomer.getAge(),
                fakedCustomer.getGender()
        );

        // Send a POST request to "api/v1/customers"
        HttpHeaders httpHeaders = webTestClient.post()
                .uri(API_URI)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders();

        // Extract JWT Token
        String token = httpHeaders.getFirst(HttpHeaders.AUTHORIZATION);

        // GET all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(API_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {})
                .returnResult()
                .getResponseBody();

        // Make sure that Customer is present
        assertThat(allCustomers).isNotNull();

        // GET customer Id
        Long id = allCustomers.stream()
                .filter(c -> c.email().equals(fakedCustomer.getEmail()))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();
        
        CustomerDTO previous = new CustomerDTO(
                id,
                fakedCustomer.getName(),
                fakedCustomer.getEmail(),
                fakedCustomer.getAge(),
                fakedCustomer.getGender()
        );
        
        assertThat(allCustomers).contains(previous);
        
        // PUT customer modified
        Customer fakedModified = ENTITY_FAKER.getCustomer();
        fakedModified.setAge(previous.age()+1);
        CustomerUpdateDTO expected = new CustomerUpdateDTO(
                fakedModified.getName(),
                fakedModified.getEmail(),
                fakedCustomer.getPassword(),
                fakedModified.getAge(),
                fakedModified.getGender()
        );
        
        webTestClient.put()
                .uri(API_URI + "/{id}", id)
                .body(Mono.just(expected), CustomerUpdateDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk();

        // GET customer modified
        CustomerDTO actual = webTestClient.get()
                .uri(API_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();
        
        // Verify that customer with same id is now updated
        assertThat(actual).isNotNull();
        assertThat(actual.id()).isEqualTo(previous.id());
        assertThat(actual.name()).isNotEqualTo(previous.name());
        assertThat(actual.email()).isNotEqualTo(previous.email());
        assertThat(actual.age()).isNotEqualTo(previous.age());
    }
}
