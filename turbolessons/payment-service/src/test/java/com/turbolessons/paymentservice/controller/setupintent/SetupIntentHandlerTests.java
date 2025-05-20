package com.turbolessons.paymentservice.controller.setupintent;


import com.turbolessons.paymentservice.dto.SetupIntentData;
import com.turbolessons.paymentservice.service.setupintent.SetupIntentService;
import com.stripe.model.SetupIntent;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@Log4j2
@WebFluxTest
@Import(SetupIntentHandlerImpl.class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml", properties = {"spring.config.name=application-test"})
public class SetupIntentHandlerTests {

    @MockBean
    private SetupIntentService setupIntentService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private SetupIntentHandler setupIntentHandler;

    @BeforeEach
    public void setUp() {
        RouterFunction<ServerResponse> routerFunction = new SetupIntentEndpointConfig(setupIntentHandler).setupIntentRoutes();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction)
                .build();
    }

    @Test
    void shouldHandleListAllSetupIntents() {

        StripeCollection<SetupIntent> setupIntents = createMockSetupIntentCollection();
        when(setupIntentService.listSetupIntents()).thenReturn(Mono.just(setupIntents));
        webTestClient.get()
                .uri("/api/payments/setupintent")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.object")
                .isEmpty()
                .jsonPath("$.data")
                .isNotEmpty();
    }

    //    Retrieve a SetupIntent
    @Test
    void shouldRetrieveSetupIntent() {

        SetupIntent setupIntent = createMockSetupIntent("si_123",
                                                        "cus_123",
                                                        "pm_123");
        when(setupIntentService.retrieveSetupIntent(anyString())).thenReturn(Mono.just(setupIntent));
        webTestClient.get()
                .uri("/api/payments/setupintent/si_123")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo(setupIntent.getId())
                .jsonPath("$.customer")
                .isEqualTo(setupIntent.getCustomer())
                .jsonPath("$.paymentMethod")
                .isEqualTo(setupIntent.getPaymentMethod())
                .jsonPath("$.description")
                .isEqualTo(setupIntent.getDescription());
    }

    //    Create a SetupIntent
    @Test
    void shouldCreateSetupIntent() {

        SetupIntent setupIntent = createMockSetupIntent("si_123",
                                                        "cus_123",
                                                        "pm_456");
        SetupIntentData dto = createMockSetupIntentDto("Test Description");
        when(setupIntentService.createSetupIntent(any(SetupIntentData.class)))
                     .thenReturn(Mono.just(setupIntent));
        webTestClient.mutateWith(mockJwt())
                .post()
                .uri("/api/payments/setupintent")
                .body(Mono.just(dto), SetupIntentData.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo(setupIntent.getId())
                .jsonPath("$.customer")
                .isEqualTo(setupIntent.getCustomer())
                .jsonPath("$.paymentMethod")
                .isEqualTo(setupIntent.getPaymentMethod())
                .jsonPath("$.description")
                .isEqualTo(setupIntent.getDescription());

    }

    //    Confirm a SetupIntent
    @Test
    void shouldConfirmSetupIntent() {

        when(setupIntentService.confirmSetupIntent(anyString()))
                     .thenReturn(Mono.empty());
        webTestClient.mutateWith(mockJwt())
                .put()
                .uri("/api/payments/setupintent/confirm/si_123")
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    //    Update a SetupIntent
    @Test
    void shouldUpdateSetupIntent() {

        when(setupIntentService.updateSetupIntent(anyString(),
                                                  any(SetupIntentData.class))).thenReturn(Mono.empty());
        webTestClient.mutateWith(mockJwt())
                .put()
                .uri("/api/payments/setupintent/si_123")
                .exchange()
                .expectStatus()
                .isNoContent();
    }


    //    Cancel a SetupIntent
    @Test
    void shouldCancelSetupIntent() {
        when(setupIntentService.cancelSetupIntent(anyString())).thenReturn(Mono.empty());
        webTestClient.mutateWith(mockJwt())
                .delete()
                .uri("/api/payments/setupintent/si_123")
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    private StripeCollection<SetupIntent> createMockSetupIntentCollection() {
        StripeCollection<SetupIntent> setupIntents = new StripeCollection<>();
        SetupIntent setupIntent1 = createMockSetupIntent("si_123",
                                                         "cus_123",
                                                         "pm_123");
        SetupIntent setupIntent2 = createMockSetupIntent("si_456",
                                                         "cus_456",
                                                         "pm_456");

        List<SetupIntent> setupIntentList = Arrays.asList(setupIntent1,
                                                          setupIntent2);
        setupIntents.setData(setupIntentList);
        return setupIntents;
    }

    private SetupIntent createMockSetupIntent(String id, String customer, String paymentMethod) {
        SetupIntent setupIntent = Mockito.mock(SetupIntent.class);
        when(setupIntent.getId()).thenReturn(id);
        when(setupIntent.getCustomer()).thenReturn(customer);
        when(setupIntent.getPaymentMethod()).thenReturn(paymentMethod);
        when(setupIntent.getDescription()).thenReturn("Test SetupIntent");
        return setupIntent;
    }

    private SetupIntentData createMockSetupIntentDto(String description) {
        return new SetupIntentData("si_123",
                                   "cus_123",
                                   "pm_456",
                                   description);
    }
}
