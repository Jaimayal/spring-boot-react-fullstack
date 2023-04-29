package com.jaimayal.journey;

import com.jaimayal.CustomEntityFaker;
import com.jaimayal.customer.Customer;
import com.jaimayal.customer.CustomerRegistrationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
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
        Customer expected = ENTITY_FAKER.getCustomer();
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                expected.getName(),
                expected.getEmail(),
                expected.getAge()
        );
        
        // Send a POST request to "api/v1/customers"
        webTestClient.post()
                .uri(API_URI)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        
        // GET all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(API_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();
        
        // Make sure that Customer is present
        assertThat(allCustomers).isNotNull();
        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expected);
        
        // GET customer by Id
        Long id = allCustomers.stream()
                .filter(c -> c.getEmail().equals(expected.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        
        expected.setId(id);
        webTestClient.get()
                .uri(API_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .isEqualTo(expected);
    }

    @Test
    void canDeleteCustomer() {
        // Create a CustomerRegistrationRequest
        Customer expected = ENTITY_FAKER.getCustomer();
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                expected.getName(),
                expected.getEmail(),
                expected.getAge()
        );

        // Send a POST request to "api/v1/customers"
        webTestClient.post()
                .uri(API_URI)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // GET all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(API_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // Make sure that Customer is present
        assertThat(allCustomers).isNotNull();
        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expected);

        // GET customer by Id
        Long id = allCustomers.stream()
                .filter(c -> c.getEmail().equals(expected.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        
        // DELETE customer by Id
        webTestClient.delete()
                .uri(API_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
        
        // Check customer is NOT FOUND
        webTestClient.get()
                .uri(API_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomer() {
        // Create a CustomerRegistrationRequest
        Customer previous = ENTITY_FAKER.getCustomer();
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                previous.getName(),
                previous.getEmail(),
                previous.getAge()
        );

        // Send a POST request to "api/v1/customers"
        webTestClient.post()
                .uri(API_URI)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // GET all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(API_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // Make sure that Customer is present
        assertThat(allCustomers).isNotNull();
        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(previous);

        // GET customer Id
        Long id = allCustomers.stream()
                .filter(c -> c.getEmail().equals(previous.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        previous.setId(id);
        
        // PUT customer modified
        Customer modified = ENTITY_FAKER.getCustomer();
        webTestClient.put()
                .uri(API_URI + "/{id}", id)
                .body(Mono.just(modified), Customer.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        // GET customer modified
        Customer actual = webTestClient.get()
                .uri(API_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();
        
        // Verify that customer with same id is now updated
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(previous.getId());
        assertThat(actual.getName()).isNotEqualTo(previous.getName());
        assertThat(actual.getEmail()).isNotEqualTo(previous.getEmail());
        assertThat(actual.getAge()).isNotEqualTo(previous.getAge());
    }
}
