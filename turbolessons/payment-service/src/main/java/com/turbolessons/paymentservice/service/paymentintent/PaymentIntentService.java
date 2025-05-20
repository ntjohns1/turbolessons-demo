package com.turbolessons.paymentservice.service.paymentintent;

import com.turbolessons.paymentservice.dto.PaymentIntentData;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeCollection;
import com.stripe.model.StripeSearchResult;
import reactor.core.publisher.Mono;

public interface PaymentIntentService {
    Mono<StripeCollection<PaymentIntent>> listAllPaymentIntents();

    Mono<PaymentIntent> retrievePaymentIntent(String id);

    Mono<StripeSearchResult<PaymentIntent>> searchPaymentIntentByCustomer(String customerId);

    Mono<PaymentIntent> createPaymentIntent(PaymentIntentData paymentIntentData);

    Mono<Void> updatePaymentIntent(String id, PaymentIntentData paymentIntentData);

    Mono<PaymentIntent> capturePaymentIntent(String id);

    Mono<Void> cancelPaymentIntent(String id);
}
