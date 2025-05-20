package com.turbolessons.paymentservice.controller.paymentmethod;


import com.turbolessons.paymentservice.service.paymentmethod.PaymentMethodService;
import com.stripe.model.PaymentMethod;
import com.stripe.model.StripeCollection;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@Log4j2
@WebFluxTest
@Import(PaymentMethodHandlerImpl.class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml", properties = {"spring.config.name=application-test"})
public class PaymentMethodHandlerTests {


    @MockBean
    private PaymentMethodService paymentMethodService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PaymentMethodHandler paymentMethodHandler;

    @BeforeEach
    public void setUp() {
        RouterFunction<ServerResponse> routerFunction = new PaymentMethodEndpointConfig(paymentMethodHandler).paymentMethodRoutes();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction)
                .build();
    }

    @Test
    void shouldRetrievePaymentMethod() {
        PaymentMethod paymentMethod = createMockCardPaymentMethod("pm_123");
        when(paymentMethodService.retrievePaymentMethod(anyString()))
                     .thenReturn(Mono.just(paymentMethod));

        webTestClient.get()
                .uri("/api/payments/paymentmethod/pm_123")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo(paymentMethod.getId());
    }

    @Test
    void shouldRetrievePaymentMethodByCustomer() {
        StripeCollection<PaymentMethod> paymentMethods = createMockStripeCollection();
        when(paymentMethodService.retrieveCustomerPaymentMethods(anyString()))
                     .thenReturn(Mono.just(paymentMethods));

        webTestClient.get()
                .uri("/api/payments/paymentmethod/customer/cus_123")
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
    void shouldAttachPaymentMethod() {

        when(paymentMethodService.attachPaymentMethod(anyString(),anyString())).thenReturn(Mono.empty());

        webTestClient.mutateWith(mockJwt())
                .put()
                .uri("/api/payments/paymentmethod/attach/pm_123/cus123")
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    void shouldDetachPaymentMethod() {

        when(paymentMethodService.detachPaymentMethod(anyString())).thenReturn(Mono.empty());

        webTestClient.mutateWith(mockJwt())
                .put()
                .uri("/api/payments/paymentmethod/detach/pm_123")
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    private StripeCollection<PaymentMethod> createMockStripeCollection() {
        StripeCollection<PaymentMethod> paymentMethods = new StripeCollection<>();

        PaymentMethod paymentMethod1 = createMockBankPaymentMethod("pm_123");
        PaymentMethod paymentMethod2 = createMockCardPaymentMethod("pm_456");

        List<PaymentMethod> paymentMethodList = Arrays.asList(paymentMethod1,
                                                              paymentMethod2);
        paymentMethods.setData(paymentMethodList);
        return paymentMethods;
    }

    private PaymentMethod createMockCardPaymentMethod(String id) {
        PaymentMethod.BillingDetails billingDetails = Mockito.mock(PaymentMethod.BillingDetails.class);
        when(billingDetails.getEmail()).thenReturn("test@example.com");
        PaymentMethod.Card card = Mockito.mock(PaymentMethod.Card.class);
        PaymentMethod paymentMethod = Mockito.mock(PaymentMethod.class);
        when(paymentMethod.getId()).thenReturn(id);
        when(paymentMethod.getBillingDetails()).thenReturn(billingDetails);
        when(paymentMethod.getCard()).thenReturn(card);
        when(paymentMethod.getType()).thenReturn("card");
        return paymentMethod;
    }

    private PaymentMethod createMockBankPaymentMethod(String id) {
        PaymentMethod.BillingDetails billingDetails = Mockito.mock(PaymentMethod.BillingDetails.class);
        PaymentMethod.UsBankAccount bank = Mockito.mock(PaymentMethod.UsBankAccount.class);
        PaymentMethod paymentMethod = Mockito.mock(PaymentMethod.class);
        when(paymentMethod.getId()).thenReturn(id);
        when(paymentMethod.getBillingDetails()).thenReturn(billingDetails);
        when(paymentMethod.getUsBankAccount()).thenReturn(bank);
        when(paymentMethod.getType()).thenReturn("us_bank_account");
        return paymentMethod;
    }
}
