package com.turbolessons.paymentservice.controller.paymentintent;

import com.turbolessons.paymentservice.dto.PaymentIntentData;
import com.turbolessons.paymentservice.service.paymentintent.PaymentIntentService;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeCollection;
import com.stripe.model.StripeSearchResult;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;


@Log4j2
@WebFluxTest
@Import(PaymentIntentHandlerImpl.class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml", properties = {"spring.config.name=application-test"})
public class PaymentIntentHandlerTests {

    @MockBean
    private PaymentIntentService paymentIntentService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PaymentIntentHandler paymentIntentHandler;

    @BeforeEach
    public void setUp() {
        RouterFunction<ServerResponse> routerFunction = new PaymentIntentEndpointConfig(paymentIntentHandler).paymentIntentRoutes();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction)
                .build();
    }

    @Test
    void shouldHandleListAllPaymentIntents() {
        StripeCollection<PaymentIntent> paymentIntents = createMockStripeCollection();
        when(paymentIntentService.listAllPaymentIntents()).thenReturn(Mono.just(paymentIntents));
        webTestClient.get()
                .uri("/api/payments/paymentintent")
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
    void shouldHandleRetrievePaymentIntent() {
        PaymentIntent paymentIntent = createMockPaymentIntent("pi_123",
                                                              5000L,
                                                              "cus_123",
                                                              "Test PaymentIntent",
                                                              "in_123",
                                                              "pm_123",
                                                              "processing");
        when(paymentIntentService.retrievePaymentIntent(anyString())).thenReturn(Mono.just(paymentIntent));
        webTestClient.get()
                .uri("/api/payments/paymentintent/pi_123")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo("pi_123")
                .jsonPath("$.amount")
                .isEqualTo(5000L)
                .jsonPath("$.currency")
                .isEqualTo("usd")
                .jsonPath("$.customer")
                .isEqualTo("cus_123")
                .jsonPath("$.description")
                .isEqualTo("Test PaymentIntent")
                .jsonPath("$.paymentMethod")
                .isEqualTo("pm_123")
                .jsonPath("$.invoice")
                .isEqualTo("in_123")
                .jsonPath("$.status")
                .isEqualTo("processing");
    }

    @Test
    void shouldHandleSearchPaymentIntentsByCustomer() {
        StripeSearchResult<PaymentIntent> mockSearchResult = createMockStripeSearchResult();
        String customerId = "cus_123";
        when(paymentIntentService.searchPaymentIntentByCustomer(customerId)).thenReturn(Mono.just(mockSearchResult));

        webTestClient.get()
                .uri("/api/payments/paymentintent/customer/" + customerId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.data[0].id")
                .isEqualTo("pi_123")
                .jsonPath("$.data[0].amount")
                .isEqualTo(5000)
                .jsonPath("$.data[0].customer")
                .isEqualTo(customerId)
                .jsonPath("$.data[0].description")
                .isEqualTo("Test PaymentIntent")
                .jsonPath("$.data[0].paymentMethod")
                .isEqualTo("pm_123")
                .jsonPath("$.data[0].invoice")
                .isEqualTo("in_123")
                .jsonPath("$.data[0].status")
                .isEqualTo("processing");
        // Additional assertions...
    }

    @Test
    void shouldHandleCreatePaymentIntent() {
        PaymentIntent paymentIntent = createMockPaymentIntent("pi_123",
                                                              5000L,
                                                              "cus_123",
                                                              "Test PaymentIntent",
                                                              "in_123",
                                                              "pm_123",
                                                              "processing");
        PaymentIntentData dto = createMockPaymentIntentDto(5000L,
                                                           "cus_123",
                                                           "Test PaymentIntent",
                                                           "pm_123");
        when(paymentIntentService.createPaymentIntent(any(PaymentIntentData.class)))
                .thenReturn(Mono.just(paymentIntent));
        webTestClient.mutateWith(mockJwt())
                .post()
                .uri("/api/payments/paymentintent")
                .body(Mono.just(dto),
                      PaymentIntentData.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo("pi_123")
                .jsonPath("$.amount")
                .isEqualTo(5000)
                .jsonPath("$.customer")
                .isEqualTo("cus_123")
                .jsonPath("$.description")
                .isEqualTo("Test PaymentIntent")
                .jsonPath("$.paymentMethod")
                .isEqualTo("pm_123")
                .jsonPath("$.invoice")
                .isEqualTo("in_123")
                .jsonPath("$.status")
                .isEqualTo("processing");
    }

    @Test
    void shouldHandleUpdatePaymentIntent() {
        PaymentIntentData updateData = createMockPaymentIntentDto(6000L,
                                                                  "cus_456",
                                                                  "Updated Test PaymentIntent",
                                                                  "pm_456");

        when(paymentIntentService.updatePaymentIntent(anyString(), any(PaymentIntentData.class)))
                .thenReturn(Mono.empty());

        webTestClient.mutateWith(mockJwt())
                .put()
                .uri("/api/payments/paymentintent/pi_123")
                .body(Mono.just(updateData), PaymentIntentData.class)
                .exchange()
                .expectStatus()
                .isNoContent();
    }


    @Test
    void shouldHandleCapturePaymentIntent() {
        PaymentIntent paymentIntent = createMockPaymentIntent("pi_123",
                                                              5000L,
                                                              "cus_123",
                                                              "Test PaymentIntent",
                                                              "in_123",
                                                              "pm_123",
                                                              "succeeded");

        when(paymentIntentService.capturePaymentIntent(anyString())).thenReturn(Mono.just(paymentIntent));

        webTestClient.mutateWith(mockJwt())
                .put()
                .uri("/api/payments/paymentintent/capture/pi_123")
                .exchange()
                .expectStatus()
                .isOk();
    }


    @Test
    void shouldHandleCancelPaymentIntent() {
        when(paymentIntentService.cancelPaymentIntent(anyString()))
                .thenReturn(Mono.empty());

        webTestClient.mutateWith(mockJwt())
                .delete()
                .uri("/api/payments/paymentintent/pi_123")
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    private StripeCollection<PaymentIntent> createMockStripeCollection() {
        StripeCollection<PaymentIntent> paymentIntents = new StripeCollection<>();

        PaymentIntent mockPaymentIntent1 = createMockPaymentIntent("pi_123",
                                                                   5000L,
                                                                   "cus_123",
                                                                   "Test PaymentIntent",
                                                                   "in_123",
                                                                   "pm_123",
                                                                   "processing");

        PaymentIntent mockPaymentIntent2 = createMockPaymentIntent("pi_456",
                                                                   6000L,
                                                                   "cus_456",
                                                                   "Test PaymentIntent 2",
                                                                   "in_456",
                                                                   "pm_456",
                                                                   "processing");


        List<PaymentIntent> paymentIntentList = Arrays.asList(mockPaymentIntent1,
                                                              mockPaymentIntent2);
        paymentIntents.setData(paymentIntentList);
        return paymentIntents;
    }

    private PaymentIntent createMockPaymentIntent(String id, Long amount, String customer, String description, String invoice, String paymentMethod, String status) {
        PaymentIntent paymentIntent = Mockito.mock(PaymentIntent.class);

        when(paymentIntent.getId()).thenReturn(id);
        when(paymentIntent.getAmount()).thenReturn(amount);
        when(paymentIntent.getCurrency()).thenReturn("usd");
        when(paymentIntent.getCustomer()).thenReturn(customer);
        when(paymentIntent.getDescription()).thenReturn(description);
        when(paymentIntent.getPaymentMethod()).thenReturn(paymentMethod);
        when(paymentIntent.getInvoice()).thenReturn(invoice);
        when(paymentIntent.getStatus()).thenReturn(status);
        return paymentIntent;
    }

    private PaymentIntentData createMockPaymentIntentDto(Long amount, String customer, String description, String paymentMethod) {
        PaymentIntentData dto = Mockito.mock(PaymentIntentData.class);

        when(dto.getAmount()).thenReturn(amount);
        when(dto.getCurrency()).thenReturn("usd");
        when(dto.getCustomer()).thenReturn(customer);
        when(dto.getDescription()).thenReturn(description);
        when(dto.getPaymentMethod()).thenReturn(paymentMethod);
        when(dto.getInvoice()).thenReturn("in_123");
        when(dto.getStatus()).thenReturn("processing");
        return dto;
    }

    private StripeSearchResult<PaymentIntent> createMockStripeSearchResult() {
        StripeSearchResult<PaymentIntent> searchResult = new StripeSearchResult<>();
        List<PaymentIntent> paymentIntentList = List.of(createMockPaymentIntent("pi_123",
                                                                                5000L,
                                                                                "cus_123",
                                                                                "Test PaymentIntent",
                                                                                "in_123",
                                                                                "pm_123",
                                                                                "processing")
                                                        // Add more mock PaymentIntents if necessary
        );
        searchResult.setData(paymentIntentList);
        return searchResult;
    }


}

