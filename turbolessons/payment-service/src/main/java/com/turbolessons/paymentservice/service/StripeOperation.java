package com.turbolessons.paymentservice.service;

import com.stripe.exception.StripeException;

@FunctionalInterface
public interface StripeOperation<T> {
    T execute() throws StripeException;
}

