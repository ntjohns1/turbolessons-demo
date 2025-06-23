package com.turbolessons.paymentservice.controller.subscription;

import com.stripe.model.StripeCollection;
import com.stripe.model.SubscriptionItem;
import com.turbolessons.paymentservice.dto.SubscriptionItemData;
import com.turbolessons.paymentservice.service.subscription.SubscriptionItemService;
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
@Import(SubscriptionItemHandlerImpl.class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml", properties = {"spring.config.name=application-test"})
public class SubscriptionItemHandlerTests {

    @MockBean
    private SubscriptionItemService subscriptionItemService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private SubscriptionItemHandlerImpl subscriptionItemHandler;

    @BeforeEach
    public void setUp() {
        RouterFunction<ServerResponse> routerFunction = new SubscriptionItemEndpointConfig().subscriptionItemRoutes(subscriptionItemHandler);
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction)
                .build();
    }

    @Test
    public void shouldHandleListAllSubscriptionItems() {
        // Given
        String subscriptionId = "sub_123";
        StripeCollection<SubscriptionItem> mockItems = createMockStripeCollection();
        when(subscriptionItemService.listSubscriptionItems(subscriptionId)).thenReturn(Mono.just(mockItems));
        
        // When/Then
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/payments/subscription_item")
                        .queryParam("subscriptionId", subscriptionId)
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.data")
                .isNotEmpty();
    }

    @Test
    void shouldHandleRetrieveSubscriptionItem() {
        // Given
        SubscriptionItem item = createMockSubscriptionItem();
        when(subscriptionItemService.retrieveSubscriptionItem(anyString())).thenReturn(Mono.just(item));
        
        // When/Then
        webTestClient.get()
                .uri("/api/payments/subscription_item/si_123")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo("si_123")
                .jsonPath("$.subscription")
                .isEqualTo("sub_123")
                .jsonPath("$.quantity")
                .isEqualTo(2);
    }

    @Test
    void shouldHandleCreateSubscriptionItem() {
        // Given
        SubscriptionItemData requestData = SubscriptionItemData.forCreate("sub_123", "price_123", 2);
        SubscriptionItemData responseData = new SubscriptionItemData(
                "si_123", "sub_123", "price_123", 2, null, null, null, null, null);
        
        when(subscriptionItemService.createSubscriptionItem(any())).thenReturn(Mono.just(responseData));
        
        // When/Then
        webTestClient
                .post()
                .uri("/api/payments/subscription_item")
                .body(Mono.just(requestData), SubscriptionItemData.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo("si_123")
                .jsonPath("$.subscription")
                .isEqualTo("sub_123")
                .jsonPath("$.price")
                .isEqualTo("price_123")
                .jsonPath("$.quantity")
                .isEqualTo(2);
    }

    @Test
    void shouldHandleUpdateSubscriptionItem() {
        // Given
        String itemId = "si_123";
        SubscriptionItemData updateData = SubscriptionItemData.forUpdate(
                itemId, "price_456", 3);
        
        when(subscriptionItemService.updateSubscriptionItem(anyString(), any(SubscriptionItemData.class)))
                .thenReturn(Mono.empty());
        
        // When/Then
        webTestClient
                .put()
                .uri("/api/payments/subscription_item/" + itemId)
                .body(Mono.just(updateData), SubscriptionItemData.class)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    void shouldHandleDeleteSubscriptionItem() {
        // Given
        String itemId = "si_123";
        when(subscriptionItemService.deleteSubscriptionItem(anyString())).thenReturn(Mono.empty());
        
        // When/Then
        webTestClient
                .delete()
                .uri("/api/payments/subscription_item/" + itemId)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    // Helper method to create mock StripeCollection
    private StripeCollection<SubscriptionItem> createMockStripeCollection() {
        StripeCollection<SubscriptionItem> items = new StripeCollection<>();

        SubscriptionItem mockItem1 = createMockSubscriptionItem();
        SubscriptionItem mockItem2 = Mockito.mock(SubscriptionItem.class);
        when(mockItem2.getId()).thenReturn("si_456");
        when(mockItem2.getSubscription()).thenReturn("sub_123");
        when(mockItem2.getQuantity()).thenReturn(1L);

        List<SubscriptionItem> itemsList = Arrays.asList(mockItem1, mockItem2);
        items.setData(itemsList);
        return items;
    }

    private SubscriptionItem createMockSubscriptionItem() {
        SubscriptionItem item = Mockito.mock(SubscriptionItem.class);
        when(item.getId()).thenReturn("si_123");
        when(item.getSubscription()).thenReturn("sub_123");
        when(item.getQuantity()).thenReturn(2L);
        return item;
    }
}
