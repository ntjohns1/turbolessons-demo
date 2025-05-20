package com.turbolessons.paymentservice.service.subscription;

import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.StripeCollection;
import com.stripe.model.SubscriptionItem;
import com.stripe.param.SubscriptionItemCreateParams;
import com.stripe.param.SubscriptionItemUpdateParams;
import com.stripe.service.SubscriptionItemService;
import com.turbolessons.paymentservice.dto.SubscriptionItemData;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionItemServiceTest {

    @Mock
    private StripeClient stripeClient;

    @Mock
    private StripeClientHelper stripeClientHelper;

    @Mock
    private SubscriptionItemService stripeSubscriptionItemService;

    @InjectMocks
    private SubscriptionItemServiceImpl subscriptionItemService;

    @Captor
    private ArgumentCaptor<SubscriptionItemCreateParams> createParamsCaptor;

    @Captor
    private ArgumentCaptor<SubscriptionItemUpdateParams> updateParamsCaptor;

    @Captor
    private ArgumentCaptor<String> subscriptionItemIdCaptor;

    @Test
    void createSubscriptionItem_shouldCreateItemWithCorrectParams() throws StripeException {
        // Arrange
        
        SubscriptionItemData itemData = SubscriptionItemData.forCreate(
                "sub_123", "price_123", 2);
        
        SubscriptionItem createdItem = mock(SubscriptionItem.class);
        when(createdItem.getId()).thenReturn("si_123");
        when(createdItem.getSubscription()).thenReturn("sub_123");
        
        Price price = mock(Price.class);
        when(price.getId()).thenReturn("price_123");
        when(createdItem.getPrice()).thenReturn(price);
        
        when(createdItem.getQuantity()).thenReturn(2L);
        
        // Mock the StripeOperation interface with proper generic type
        when(stripeClientHelper.<SubscriptionItem>executeStripeCall(any(StripeOperation.class)))
            .thenReturn(Mono.just(createdItem));

        // Act
        Mono<SubscriptionItemData> result = subscriptionItemService.createSubscriptionItem(itemData);

        // Assert
        StepVerifier.create(result)
                .assertNext(data -> {
                    assertEquals("si_123", data.id());
                    assertEquals("sub_123", data.subscription());
                    assertEquals("price_123", data.price());
                    assertEquals(2, data.quantity());
                })
                .verifyComplete();

        verify(stripeClientHelper).executeStripeCall(any(StripeOperation.class));
    }

    @Test
    void retrieveSubscriptionItem_shouldReturnItemById() throws StripeException {
        // Arrange
        
        
        String itemId = "si_123";
        SubscriptionItem item = mock(SubscriptionItem.class);
        
        // Mock the StripeOperation interface with proper generic type
        when(stripeClientHelper.<SubscriptionItem>executeStripeCall(any(StripeOperation.class)))
            .thenReturn(Mono.just(item));

        // Act
        Mono<SubscriptionItem> result = subscriptionItemService.retrieveSubscriptionItem(itemId);

        // Assert
        StepVerifier.create(result)
                .expectNext(item)
                .verifyComplete();

        verify(stripeClientHelper).executeStripeCall(any(StripeOperation.class));
    }

    @Test
    void updateSubscriptionItem_shouldUpdateWithCorrectParams() throws StripeException {
        // Arrange
        
        
        String itemId = "si_123";
        SubscriptionItemData updateData = SubscriptionItemData.forUpdate(
                itemId, "price_456", 3);
        
        // Mock the StripeVoidOperation interface
        when(stripeClientHelper.executeStripeVoidCall(any(StripeVoidOperation.class)))
            .thenReturn(Mono.empty());

        // Act
        Mono<Void> result = subscriptionItemService.updateSubscriptionItem(itemId, updateData);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(stripeClientHelper).executeStripeVoidCall(any(StripeVoidOperation.class));
    }

    @Test
    void updateSubscriptionItem_withTaxRates_shouldUpdateWithCorrectParams() throws StripeException {
        // Arrange
        
        
        String itemId = "si_123";
        SubscriptionItemData updateData = new SubscriptionItemData(
                itemId, null, "price_456", 3, null, null, 
                new String[]{"txr_123", "txr_456"}, null, null);
        
        // Mock the StripeVoidOperation interface
        when(stripeClientHelper.executeStripeVoidCall(any(StripeVoidOperation.class)))
            .thenReturn(Mono.empty());

        // Act
        Mono<Void> result = subscriptionItemService.updateSubscriptionItem(itemId, updateData);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(stripeClientHelper).executeStripeVoidCall(any(StripeVoidOperation.class));
    }

    @Test
    void deleteSubscriptionItem_shouldDeleteItemById() throws StripeException {
        // Arrange
        
        
        String itemId = "si_123";
        
        // Mock the StripeVoidOperation interface
        when(stripeClientHelper.executeStripeVoidCall(any(StripeVoidOperation.class)))
            .thenReturn(Mono.empty());

        // Act
        Mono<Void> result = subscriptionItemService.deleteSubscriptionItem(itemId);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(stripeClientHelper).executeStripeVoidCall(any(StripeVoidOperation.class));
    }

    @Test
    void listSubscriptionItems_shouldReturnItemsForSubscription() throws StripeException {
        // Arrange
        
        
        String subscriptionId = "sub_123";
        @SuppressWarnings("unchecked")
        StripeCollection<SubscriptionItem> itemsCollection = mock(StripeCollection.class);
        
        // Mock the StripeOperation interface with proper generic type
        when(stripeClientHelper.<StripeCollection<SubscriptionItem>>executeStripeCall(any(StripeOperation.class)))
            .thenReturn(Mono.just(itemsCollection));

        // Act
        Mono<StripeCollection<SubscriptionItem>> result = subscriptionItemService.listSubscriptionItems(subscriptionId);

        // Assert
        StepVerifier.create(result)
                .expectNext(itemsCollection)
                .verifyComplete();

        verify(stripeClientHelper).executeStripeCall(any(StripeOperation.class));
    }
}