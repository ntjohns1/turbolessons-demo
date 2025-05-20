package com.turbolessons.paymentservice.service;

import com.stripe.exception.StripeException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class StripeClientHelperImpl implements StripeClientHelper {

    @Override
    public <T> Mono<T> executeStripeCall(StripeOperation<T> stripeOperation) {
        return Mono.fromCallable(() -> {
            try {
                return stripeOperation.execute();
            } catch (StripeException e) {
                throw new Exception("Error processing Stripe API", e);
            }
        }).onErrorMap(ex -> {
            if (ex.getCause() instanceof StripeException) {
                return new Exception("Error processing Stripe API", ex.getCause());
            }
            return ex;
        });
    }

    @Override
    public Mono<Void> executeStripeVoidCall(StripeVoidOperation stripeOperation) {
        return Mono.fromRunnable(() -> {
            try {
                stripeOperation.execute();
            } catch (StripeException e) {
                throw new RuntimeException("Error processing Stripe API", e);
            }
        }).onErrorMap(ex -> {
            if (ex.getCause() instanceof StripeException) {
                return new Exception("Error processing Stripe API", ex.getCause());
            }
            return ex;
        }).then();
    }

}


