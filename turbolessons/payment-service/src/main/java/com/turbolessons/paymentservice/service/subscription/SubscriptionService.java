package com.turbolessons.paymentservice.service.subscription;

import com.turbolessons.paymentservice.dto.SubscriptionData;
import com.stripe.model.StripeCollection;
import com.stripe.model.StripeSearchResult;
import com.stripe.model.Subscription;
import reactor.core.publisher.Mono;

public interface SubscriptionService {
    //    List all Subscriptions
    Mono<StripeCollection<Subscription>> listAllSubscriptions();

    //    Retrieve a Subscription by id
    Mono<Subscription> retrieveSubscription(String id);

    //    Search Subscriptions by Customer
    Mono<StripeSearchResult<Subscription>> getSubscriptionsByCustomer(String customerId);

    //    Create a Subscription
    Mono<SubscriptionData> createSubscription(SubscriptionData subscriptionData);

    //    Update a Subscription
    Mono<Void> updateSubscription(String id, SubscriptionData subscriptionData);

    //    Cancel a Subscription
    Mono<Void> cancelSubscription(String id);
}
