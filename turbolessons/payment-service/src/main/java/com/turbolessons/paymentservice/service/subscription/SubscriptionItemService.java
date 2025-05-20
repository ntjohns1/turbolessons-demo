package com.turbolessons.paymentservice.service.subscription;

import com.stripe.model.StripeCollection;
import com.stripe.model.SubscriptionItem;
import com.turbolessons.paymentservice.dto.SubscriptionItemData;
import reactor.core.publisher.Mono;

public interface SubscriptionItemService {
    
    // Create a subscription item
    Mono<SubscriptionItemData> createSubscriptionItem(SubscriptionItemData subscriptionItemData);
    
    // Retrieve a subscription item by id
    Mono<SubscriptionItem> retrieveSubscriptionItem(String id);
    
    // Update a subscription item
    Mono<Void> updateSubscriptionItem(String id, SubscriptionItemData subscriptionItemData);
    
    // Delete a subscription item
    Mono<Void> deleteSubscriptionItem(String id);
    
    // List all subscription items for a subscription
    Mono<StripeCollection<SubscriptionItem>> listSubscriptionItems(String subscriptionId);
}
