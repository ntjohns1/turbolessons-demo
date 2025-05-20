package com.turbolessons.paymentservice.service.paymentintent;

import com.turbolessons.paymentservice.dto.PaymentIntentData;
import com.turbolessons.paymentservice.service.StripeClientHelper;
import com.stripe.StripeClient;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeCollection;
import com.stripe.model.StripeSearchResult;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentSearchParams;
import com.stripe.param.PaymentIntentUpdateParams;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PaymentIntentServiceImpl implements PaymentIntentService {

    private final StripeClient stripeClient;

    private final StripeClientHelper stripeClientHelper;


    public PaymentIntentServiceImpl(StripeClient stripeClient, StripeClientHelper stripeClientHelper) {
        this.stripeClient = stripeClient;
        this.stripeClientHelper = stripeClientHelper;
    }

    @Override
    public Mono<StripeCollection<PaymentIntent>> listAllPaymentIntents() {
        return stripeClientHelper.executeStripeCall(() -> stripeClient.paymentIntents().list());
    }

    @Override
    public Mono<PaymentIntent> retrievePaymentIntent(String id) {

        return stripeClientHelper.executeStripeCall(() -> stripeClient.paymentIntents()
                        .retrieve(id));
    }

    @Override
    public Mono<StripeSearchResult<PaymentIntent>> searchPaymentIntentByCustomer(String customerId) {

        return stripeClientHelper.executeStripeCall(() -> stripeClient.paymentIntents()
                .search(PaymentIntentSearchParams.builder()
                                .setQuery(String.format("customer:%s",
                                                        customerId))
                                .build()));
    }

    @Override
    public Mono<PaymentIntent> createPaymentIntent(PaymentIntentData paymentIntentData) {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(paymentIntentData.getAmount())
                .setAutomaticPaymentMethods(PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                                    .setEnabled(true)
                                                    .build())
                .setCustomer(paymentIntentData.getCustomer())
                .setCurrency("usd")

                .build();

        return stripeClientHelper.executeStripeCall(() -> stripeClient.paymentIntents()
                .create(params));
    }

    @Override
    public Mono<Void> updatePaymentIntent(String id, PaymentIntentData paymentIntentData) {
        PaymentIntentUpdateParams params = PaymentIntentUpdateParams.builder()
                .setAmount(paymentIntentData.getAmount())
                .setCustomer(paymentIntentData.getCustomer())
                .setDescription(paymentIntentData.getDescription())
                .setPaymentMethod(paymentIntentData.getPaymentMethod())
                .build();

        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.paymentIntents()
                .update(id,
                        params));
    }

    @Override
    public Mono<PaymentIntent> capturePaymentIntent(String id) {

        return stripeClientHelper.executeStripeCall(() -> stripeClient.paymentIntents()
                .capture(id));
    }

    @Override
    public Mono<Void> cancelPaymentIntent(String id) {

        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.paymentIntents()
                .cancel(id));
    }
}
