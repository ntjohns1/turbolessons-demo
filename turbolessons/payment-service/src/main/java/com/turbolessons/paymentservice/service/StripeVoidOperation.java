package com.turbolessons.paymentservice.service;

import com.stripe.exception.StripeException;

@FunctionalInterface
public interface StripeVoidOperation {
    void execute() throws StripeException;
}

