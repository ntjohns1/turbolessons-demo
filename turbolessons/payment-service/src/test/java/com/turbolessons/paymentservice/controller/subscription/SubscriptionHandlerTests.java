package com.turbolessons.paymentservice.controller.subscription;

import com.stripe.model.StripeCollection;
import com.stripe.model.Subscription;
import com.turbolessons.paymentservice.dto.SubscriptionData;
import com.turbolessons.paymentservice.service.subscription.SubscriptionService;
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

@Log4j2
@WebFluxTest
@Import(SubscriptionHandlerImpl.class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml", properties = {"spring.config.name=application-test"})
public class SubscriptionHandlerTests {


    @MockBean
    private SubscriptionService subscriptionService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private SubscriptionHandlerImpl subscriptionHandler;

    private final Long testDate = 12345678L;

    @BeforeEach
    public void setUp() {
        RouterFunction<ServerResponse> routerFunction = new SubscriptionEndpointConfig(subscriptionHandler).subscriptionRoutes();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction)
                .build();
    }

    @Test
    public void shouldHandleListAllSubscriptions() {
        StripeCollection<Subscription> mockSubscriptions = createMockStripeCollection();
        when(subscriptionService.listAllSubscriptions()).thenReturn(Mono.just(mockSubscriptions));
        System.out.println(mockSubscriptions);
        webTestClient.get()
                .uri("/api/payments/subscription")
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
    void shouldHandleRetrieveSubscription() {
        Subscription subscription = createMockSubscription();
        when(subscriptionService.retrieveSubscription(anyString())).thenReturn(Mono.just(subscription));
        webTestClient.get()
                .uri("/api/payments/subscription/cus_123")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo("sub_123")
                .jsonPath("$.customer")
                .isEqualTo("cus_123")
                .jsonPath("$.cancelAtPeriodEnd")
                .isEqualTo(false)
                .jsonPath("$.cancelAt")
                .isEqualTo(testDate)
                .jsonPath("$.defaultPaymentMethod")
                .isEqualTo("pm_123");
    }

    @Test
    void shouldHandleCreateSubscription() {
        SubscriptionData data = createSubscriptionDto();
        when(subscriptionService.createSubscription(any())).thenReturn(Mono.just(data));
        webTestClient
                .post()
                .uri("/api/payments/subscription")
                .body(Mono.just(data),
                      SubscriptionData.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo("sub_123")
                .jsonPath("$.customer")
                .isEqualTo("cus_123")
                .jsonPath("$.cancelAtPeriodEnd")
                .isEqualTo(false)
                .jsonPath("$.cancelAt")
                .isEqualTo(testDate)
                .jsonPath("$.defaultPaymentMethod")
                .isEqualTo("pm_123");
    }

    @Test
    void shouldHandleUpdateSubscription() {
        List<String> items = List.of("price_123",
                                     "price_234");
        SubscriptionData updateData = new SubscriptionData("sub_123",
                                                           "cust_123",
                                                           items,
                                                           true,
                                                           12345678L,
                                                           "pm_456");
        when(subscriptionService.updateSubscription(anyString(),
                                                    any(SubscriptionData.class))).thenReturn(Mono.empty());

        webTestClient
                .put()
                .uri("/api/payments/subscription/sub_123")
                .body(Mono.just(updateData),
                      SubscriptionData.class)
                .exchange()
                .expectStatus()
                .isNoContent();

    }

    @Test
    void shouldHandleDeleteSubscription() {
        when(subscriptionService.cancelSubscription(anyString())).thenReturn(Mono.empty());

        webTestClient
                .delete()
                .uri("/api/payments/subscription/cus_123")
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    // Helper method to create mock StripeCollection
    private StripeCollection<Subscription> createMockStripeCollection() {
        StripeCollection<Subscription> subscriptions = new StripeCollection<>();

        Subscription mockSubscription1 = Mockito.mock(Subscription.class);
        when(mockSubscription1.getId()).thenReturn("cus_123");

        Subscription mockSubscription2 = Mockito.mock(Subscription.class);
        when(mockSubscription2.getId()).thenReturn("cus_456");

        List<Subscription> subscriptionList = Arrays.asList(mockSubscription1,
                                                            mockSubscription2);
        subscriptions.setData(subscriptionList);
        return subscriptions;
    }

    private Subscription createMockSubscription() {
        Subscription subscription = Mockito.mock(Subscription.class);
        when(subscription.getId()).thenReturn("sub_123");
        when(subscription.getCancelAtPeriodEnd()).thenReturn(false);
        when(subscription.getCancelAt()).thenReturn(testDate);
        when(subscription.getCustomer()).thenReturn("cus_123");
        when(subscription.getDefaultPaymentMethod()).thenReturn("pm_123");
        return subscription;
    }

    private SubscriptionData createSubscriptionDto() {
        List<String> items = List.of("price_123",
                                     "price_234");
        return new SubscriptionData("sub_123",
                                    "cus_123",
                                    items,
                                    false,
                                    12345678L,
                                    "pm_123");
    }
}
