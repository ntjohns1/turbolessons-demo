package com.turbolessons.paymentservice.service.paymentmethod;

import com.turbolessons.paymentservice.service.StripeClientHelper;
import com.stripe.StripeClient;
import com.stripe.model.PaymentMethod;
import com.stripe.model.StripeCollection;
import com.stripe.param.PaymentMethodAttachParams;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final StripeClient stripeClient;
    private final StripeClientHelper stripeClientHelper;

    public PaymentMethodServiceImpl(StripeClient stripeClient, StripeClientHelper stripeClientHelper) {
        this.stripeClient = stripeClient;
        this.stripeClientHelper = stripeClientHelper;
    }

    @Override
    public Mono<PaymentMethod> retrievePaymentMethod(String id) {
//
        return stripeClientHelper.executeStripeCall(() -> stripeClient.paymentMethods()
                .retrieve(id));
    }

    @Override
    public Mono<StripeCollection<PaymentMethod>> retrieveCustomerPaymentMethods(String customerId) {

        return stripeClientHelper.executeStripeCall(() -> stripeClient.customers()
                .paymentMethods()
                .list(customerId));
    }

    @Override
    public Mono<Void> attachPaymentMethod(String id, String customerId) {
        PaymentMethodAttachParams params = PaymentMethodAttachParams.builder()
                .setCustomer(customerId)
                .build();

        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.paymentMethods()
                .attach(id,
                        params));
    }

    @Override
    public Mono<Void> detachPaymentMethod(String id) {

        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.paymentMethods()
                .detach(id));
    }
}
