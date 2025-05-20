package com.turbolessons.paymentservice.service.subscription;

import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.StripeCollection;
import com.stripe.model.StripeSearchResult;
import com.stripe.model.Subscription;
import com.stripe.model.SubscriptionItem;
import com.stripe.model.SubscriptionItemCollection;
import com.stripe.param.SubscriptionCreateParams;
import com.stripe.param.SubscriptionSearchParams;
import com.stripe.param.SubscriptionUpdateParams;
import com.turbolessons.paymentservice.dto.SubscriptionData;
import com.turbolessons.paymentservice.service.StripeClientHelper;
import com.turbolessons.paymentservice.service.StripeOperation;
import com.turbolessons.paymentservice.service.StripeVoidOperation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {

    @Mock
    private StripeClient stripeClient;

    @Mock
    private StripeClientHelper stripeClientHelper;

    @Mock
    private com.stripe.service.SubscriptionService stripeSubscriptionService;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    @Captor
    private ArgumentCaptor<SubscriptionCreateParams> createParamsCaptor;

    @Captor
    private ArgumentCaptor<SubscriptionUpdateParams> updateParamsCaptor;

    @Captor
    private ArgumentCaptor<String> subscriptionIdCaptor;

    @Captor
    private ArgumentCaptor<SubscriptionSearchParams> searchParamsCaptor;

    @Test
    void listAllSubscriptions_shouldReturnAllSubscriptions() throws StripeException {
        // Arrange
        @SuppressWarnings("unchecked")
        StripeCollection<Subscription> subscriptions = mock(StripeCollection.class);
        
        // Mock the StripeOperation interface with proper generic type
        when(stripeClientHelper.<StripeCollection<Subscription>>executeStripeCall(any(StripeOperation.class)))
            .thenReturn(Mono.just(subscriptions));

        // Act
        Mono<StripeCollection<Subscription>> result = subscriptionService.listAllSubscriptions();

        // Assert
        StepVerifier.create(result)
                .expectNext(subscriptions)
                .verifyComplete();

        verify(stripeClientHelper).executeStripeCall(any(StripeOperation.class));
    }

    @Test
    void retrieveSubscription_shouldReturnSubscriptionById() throws StripeException {
        // Arrange
        String subscriptionId = "sub_123";
        Subscription subscription = mock(Subscription.class);
        
        // Mock the StripeOperation interface with proper generic type
        when(stripeClientHelper.<Subscription>executeStripeCall(any(StripeOperation.class)))
            .thenReturn(Mono.just(subscription));

        // Act
        Mono<Subscription> result = subscriptionService.retrieveSubscription(subscriptionId);

        // Assert
        StepVerifier.create(result)
                .expectNext(subscription)
                .verifyComplete();

        verify(stripeClientHelper).executeStripeCall(any(StripeOperation.class));
    }

    @Test
    void getSubscriptionsByCustomer_shouldReturnSubscriptionsForCustomer() throws StripeException {
        // Arrange
        String customerId = "cus_123";
        @SuppressWarnings("unchecked")
        StripeSearchResult<Subscription> searchResult = mock(StripeSearchResult.class);
        
        // Mock the StripeOperation interface with proper generic type
        when(stripeClientHelper.<StripeSearchResult<Subscription>>executeStripeCall(any(StripeOperation.class)))
            .thenReturn(Mono.just(searchResult));

        // Act
        Mono<StripeSearchResult<Subscription>> result = subscriptionService.getSubscriptionsByCustomer(customerId);

        // Assert
        StepVerifier.create(result)
                .expectNext(searchResult)
                .verifyComplete();

        verify(stripeClientHelper).executeStripeCall(any(StripeOperation.class));
    }

    @Test
    void createSubscription_shouldCreateSubscriptionWithCorrectParams() throws StripeException {
        // Arrange
        SubscriptionData subscriptionData = new SubscriptionData();
        subscriptionData.setCustomer("cus_123");
        subscriptionData.setCancelAtPeriodEnd(false);
        subscriptionData.setCancelAt(null);
        subscriptionData.setDefaultPaymentMethod("pm_123");
        subscriptionData.setItems(Arrays.asList("price_123", "price_456"));

        Subscription subscription = mock(Subscription.class);
        when(subscription.getId()).thenReturn("sub_123");
        when(subscription.getCustomer()).thenReturn("cus_123");
        when(subscription.getCancelAtPeriodEnd()).thenReturn(false);
        when(subscription.getCanceledAt()).thenReturn(null);
        when(subscription.getDefaultPaymentMethod()).thenReturn("pm_123");

        // Mock subscription items
        SubscriptionItemCollection itemsCollection = mock(SubscriptionItemCollection.class);
        List<SubscriptionItem> itemsList = new ArrayList<>();
        SubscriptionItem item1 = mock(SubscriptionItem.class);
        when(item1.getId()).thenReturn("si_123");
        itemsList.add(item1);
        when(itemsCollection.getData()).thenReturn(itemsList);
        when(subscription.getItems()).thenReturn(itemsCollection);

        // Mock the StripeOperation interface with proper generic type
        when(stripeClientHelper.<Subscription>executeStripeCall(any(StripeOperation.class)))
            .thenReturn(Mono.just(subscription));

        // Act
        Mono<SubscriptionData> result = subscriptionService.createSubscription(subscriptionData);

        // Assert
        StepVerifier.create(result)
                .assertNext(data -> {
                    assertEquals("sub_123", data.getId());
                    assertEquals("cus_123", data.getCustomer());
                    assertEquals(1, data.getItems().size());
                    assertEquals("si_123", data.getItems().get(0));
                    assertEquals(false, data.getCancelAtPeriodEnd());
                    assertEquals(null, data.getCancelAt());
                    assertEquals("pm_123", data.getDefaultPaymentMethod());
                })
                .verifyComplete();

        verify(stripeClientHelper).executeStripeCall(any(StripeOperation.class));
    }

    @Test
    void updateSubscription_shouldUpdateSubscriptionWithCorrectParams() throws StripeException {
        // Arrange
        String subscriptionId = "sub_123";
        SubscriptionData subscriptionData = new SubscriptionData();
        subscriptionData.setCancelAtPeriodEnd(true);
        subscriptionData.setCancelAt(1234567890L);
        subscriptionData.setDefaultPaymentMethod("pm_456");

        // Mock the StripeVoidOperation interface
        when(stripeClientHelper.executeStripeVoidCall(any(StripeVoidOperation.class)))
            .thenReturn(Mono.empty());

        // Act
        Mono<Void> result = subscriptionService.updateSubscription(subscriptionId, subscriptionData);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(stripeClientHelper).executeStripeVoidCall(any(StripeVoidOperation.class));
    }

    @Test
    void cancelSubscription_shouldCancelSubscriptionById() throws StripeException {
        // Arrange
        String subscriptionId = "sub_123";
        
        // Mock the StripeVoidOperation interface
        when(stripeClientHelper.executeStripeVoidCall(any(StripeVoidOperation.class)))
            .thenReturn(Mono.empty());

        // Act
        Mono<Void> result = subscriptionService.cancelSubscription(subscriptionId);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(stripeClientHelper).executeStripeVoidCall(any(StripeVoidOperation.class));
    }
}