package com.jaimayal.journey;

import com.jaimayal.utils.CustomEntityFaker;
import com.jaimayal.customer.Customer;
import com.jaimayal.customer.CustomerDTO;
import com.jaimayal.customer.CustomerRegistrationRequest;
import com.jaimayal.customer.CustomerUpdateDTO;
import com.jaimayal.utils.IntegrationUtils;
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
    private static final IntegrationUtils INTEGRATION_UTILS = new IntegrationUtils();
    
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
        // Register first customer. To Delete
        CustomerRegistrationRequest toDelete = ENTITY_FAKER.getCustomerRegistrationRequest();
        INTEGRATION_UTILS.registerNewCustomerAndGetToken(
                toDelete,
                webTestClient,
                API_URI
        );
        
        // Register second customer. Used to query the API
        CustomerRegistrationRequest usedToQuery = ENTITY_FAKER.getCustomerRegistrationRequest();
        String token = INTEGRATION_UTILS.registerNewCustomerAndGetToken(
                usedToQuery,
                webTestClient,
                API_URI
        );

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

        // Make sure that customers list is not null
        assertThat(allCustomers).isNotNull();

        // Extract toDelete customer Id
        Long id = allCustomers.stream()
                .filter(c -> c.email().equals(toDelete.email()))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();
        
        // DELETE toDelete by Id
        webTestClient.delete()
                .uri(API_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchange()
                .expectStatus()
                .isNoContent();
        
        // GET toDelete, should be NOT FOUND
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
        // Register first customer. toUpdate
        CustomerRegistrationRequest toUpdate = ENTITY_FAKER.getCustomerRegistrationRequest();
        INTEGRATION_UTILS.registerNewCustomerAndGetToken(
                toUpdate,
                webTestClient,
                API_URI
        );

        // Register second customer. Used to query the API
        CustomerRegistrationRequest usedToQuery = ENTITY_FAKER.getCustomerRegistrationRequest();
        String token = INTEGRATION_UTILS.registerNewCustomerAndGetToken(
                usedToQuery,
                webTestClient,
                API_URI
        );

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

        // Make sure that customers list is not null
        assertThat(allCustomers).isNotNull();

        // Extract toUpdate registered customer
        CustomerDTO previous = allCustomers.stream()
                .filter(c -> c.email().equals(toUpdate.email()))
                .findFirst()
                .orElseThrow();
        
        // Create update request
        CustomerUpdateDTO updateRequest = ENTITY_FAKER.getCustomerUpdateDTO(previous);

        // PUT toUpdate modification
        webTestClient.put()
                .uri(API_URI + "/{id}", previous.id())
                .body(Mono.just(updateRequest), CustomerUpdateDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk();

        // GET customer modified
        CustomerDTO actual = webTestClient.get()
                .uri(API_URI + "/{id}", previous.id())
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
        assertThat(actual.gender()).isNotEqualTo(previous.gender());
    }
}
