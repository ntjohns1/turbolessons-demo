package com.turbolessons.paymentservice.controller.customer;

import com.stripe.model.Customer;
import com.stripe.model.StripeCollection;
import com.turbolessons.paymentservice.dto.Address;
import com.turbolessons.paymentservice.dto.CustomerData;
import com.turbolessons.paymentservice.service.customer.CustomerService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@Log4j2
@WebFluxTest
@Import(CustomerHandlerImpl.class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml", properties = {"spring.config.name=application-test"})
public class CustomerHandlerTests {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CustomerHandlerImpl customerHandler;

    @BeforeEach
    public void setUp() {
        RouterFunction<ServerResponse> routerFunction = new CustomerEndpointConfig(customerHandler).customerRoutes();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction)
                .build();
    }

    @Test
    public void shouldHandleListAllCustomers() {
        StripeCollection<Customer> mockCustomers = createMockStripeCollection();
        when(customerService.listAllCustomers()).thenReturn(Mono.just(mockCustomers));
        webTestClient.get()
                .uri("/api/payments/customer")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.object")
                .isEmpty()
                .jsonPath("$.data")
                .isNotEmpty();
    }

    @Test
    void shouldHandleRetrieveCustomer() {
        CustomerData customer = createCustomerDto();
        when(customerService.retrieveCustomer(anyString())).thenReturn(Mono.just(customer));
        webTestClient.get()
                .uri("/api/payments/customer/cus_123")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo(customer.getId())
                .jsonPath("$.email")
                .isEqualTo("email@example.com")
                .jsonPath("$.name")
                .isEqualTo("Claudia Coulthard");
    }

    @Test
    void shouldHandleSearchBySystemId() {
        CustomerData customer = createCustomerDto();
        Map<String, String> metadata = new HashMap<>();
        metadata.put("okta_id",
                     "00u75cn4yauHU7bl55d7");
        customer.setMetadata(metadata);
        when(customerService.searchCustomerBySystemId(anyString())).thenReturn(Mono.just(customer));

        webTestClient.get()
                .uri("/api/payments/customer/lookup/00u75cn4yauHU7bl55d7")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo(customer.getId())
                .jsonPath("$.email")
                .isEqualTo("email@example.com")
                .jsonPath("$.name")
                .isEqualTo("Claudia Coulthard");
    }

    @Test
    void shouldHandleCreateCustomer() {
        CustomerData data = createCustomerDto();
        when(customerService.createCustomer(any())).thenReturn(Mono.just(data));

        webTestClient.mutateWith(mockJwt())
                .post()
                .uri("/api/payments/customer")
                .body(Mono.just(data),
                      CustomerData.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo("cus_123")
                .jsonPath("$.email")
                .isEqualTo("email@example.com")
                .jsonPath("$.name")
                .isEqualTo("Claudia Coulthard");
    }

    @Test
    void shouldHandleUpdateCustomer() {
        CustomerData data = createCustomerDto();

        when(customerService.updateCustomer(anyString(),
                                            any(CustomerData.class))).thenReturn(Mono.empty());

        webTestClient.mutateWith(mockJwt())
                .put()
                .uri("/api/payments/customer/cus_123")
                .body(Mono.just(data),
                      CustomerData.class)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    void shouldHandleDeleteCustomer() {
        when(customerService.deleteCustomer(anyString())).thenReturn(Mono.empty());

        webTestClient.mutateWith(mockJwt())
                .delete()
                .uri("/api/payments/customer/cus_123")
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    // Helper method to create mock StripeCollection
    private StripeCollection<Customer> createMockStripeCollection() {
        StripeCollection<Customer> customers = new StripeCollection<>();

        Customer mockCustomer1 = createMockCustomer("test@example.com",
                                                    "Andrew Anderson");

        Customer mockCustomer2 = createMockCustomer("test@example.com",
                                                    "Claudia Coulthard");

        List<Customer> customerList = Arrays.asList(mockCustomer1,
                                                    mockCustomer2);
        customers.setData(customerList);
        return customers;
    }

    private Customer createMockCustomer(String email, String name) {
        Customer customer = Mockito.mock(Customer.class);
        when(customer.getId()).thenReturn("cus_123");
        when(customer.getEmail()).thenReturn(email);
        when(customer.getName()).thenReturn(name);
        return customer;
    }


    private CustomerData createCustomerDto() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("okta_id",
                     "00u75cn4yauHU7bl55d7");

        Address address = new Address();
        address.setCity("Los Angeles");
        address.setState("CA");
        address.setCountry("US");
        address.setLine1("123 Easy St.");
        address.setLine2("APT A");
        address.setPostalCode("12345");

        List<String> subscriptions = List.of("sub_123");

        return new CustomerData("cus_123",
                                address,
                                "email@example.com",
                                "Claudia Coulthard",
                                "1234567890",
                                "pm_789",
                                "Test DTO",
                                metadata,
                                subscriptions
                                // Add subscriptions
        );
    }
};
