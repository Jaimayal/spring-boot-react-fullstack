package com.jaimayal.utils;

import com.jaimayal.customer.Customer;
import com.jaimayal.customer.CustomerRegistrationRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

public class IntegrationUtils {

    private static final CustomEntityFaker ENTITY_FAKER = new CustomEntityFaker();
    
    public String registerNewCustomerAndGetToken(CustomerRegistrationRequest request, 
                                                 WebTestClient webTestClient, 
                                                 String API_URI) {
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
        return httpHeaders.getFirst(HttpHeaders.AUTHORIZATION);
    }
}
