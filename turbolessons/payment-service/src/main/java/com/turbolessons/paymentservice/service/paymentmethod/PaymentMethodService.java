package com.turbolessons.paymentservice.service.paymentmethod;

import com.stripe.model.PaymentMethod;
import com.stripe.model.StripeCollection;
import reactor.core.publisher.Mono;

public interface PaymentMethodService {
    //    Retrieve a PaymentMethod
    Mono<PaymentMethod> retrievePaymentMethod(String id);

    Mono<StripeCollection<PaymentMethod>> retrieveCustomerPaymentMethods(String customerId);

    Mono<Void> attachPaymentMethod(String id, String customerId);

    Mono<Void> detachPaymentMethod(String id);
}
