package com.turbolessons.paymentservice.service;

import com.stripe.exception.StripeException;
import com.stripe.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Mock implementation of Stripe service operations for demo environment
 * This service will be used instead of making actual Stripe API calls
 */
@Service
@Profile("demo")
@Slf4j
public class MockStripeService {

    /**
     * Create a mock customer with dummy data
     */
    public Mono<Customer> createCustomer(String email, String name) {
        log.info("Creating mock customer with email: {}, name: {}", email, name);
        
        Customer customer = new Customer();
        Map<String, Object> customerParams = new HashMap<>();
        customerParams.put("id", "cus_mock_" + UUID.randomUUID().toString().substring(0, 8));
        customerParams.put("email", email);
        customerParams.put("name", name);
        customerParams.put("description", "Mock customer for demo");
        
        try {
            customer.update(customerParams);
            return Mono.just(customer);
        } catch (StripeException e) {
            log.error("Error creating mock customer", e);
            return Mono.error(e);
        }
    }
    
    /**
     * Create a mock payment method with dummy data
     */
    public Mono<PaymentMethod> createPaymentMethod(String customerId) {
        log.info("Creating mock payment method for customer: {}", customerId);
        
        PaymentMethod paymentMethod = new PaymentMethod();
        Map<String, Object> paymentMethodParams = new HashMap<>();
        paymentMethodParams.put("id", "pm_mock_" + UUID.randomUUID().toString().substring(0, 8));
        paymentMethodParams.put("type", "card");
        
        Map<String, Object> cardData = new HashMap<>();
        cardData.put("brand", "visa");
        cardData.put("last4", "4242");
        cardData.put("exp_month", 12);
        cardData.put("exp_year", 2025);
        paymentMethodParams.put("card", cardData);
        
        try {
            paymentMethod.update(paymentMethodParams);
            return Mono.just(paymentMethod);
        } catch (StripeException e) {
            log.error("Error creating mock payment method", e);
            return Mono.error(e);
        }
    }
    
    /**
     * Create a mock payment intent with dummy data
     */
    public Mono<PaymentIntent> createPaymentIntent(Long amount, String currency, String customerId) {
        log.info("Creating mock payment intent for customer: {}, amount: {}, currency: {}", 
                customerId, amount, currency);
        
        PaymentIntent paymentIntent = new PaymentIntent();
        Map<String, Object> paymentIntentParams = new HashMap<>();
        paymentIntentParams.put("id", "pi_mock_" + UUID.randomUUID().toString().substring(0, 8));
        paymentIntentParams.put("amount", amount);
        paymentIntentParams.put("currency", currency);
        paymentIntentParams.put("customer", customerId);
        paymentIntentParams.put("status", "succeeded");
        
        try {
            paymentIntent.update(paymentIntentParams);
            return Mono.just(paymentIntent);
        } catch (StripeException e) {
            log.error("Error creating mock payment intent", e);
            return Mono.error(e);
        }
    }
    
    /**
     * Create a mock product with dummy data
     */
    public Mono<Product> createProduct(String name, String description) {
        log.info("Creating mock product with name: {}", name);
        
        Product product = new Product();
        Map<String, Object> productParams = new HashMap<>();
        productParams.put("id", "prod_mock_" + UUID.randomUUID().toString().substring(0, 8));
        productParams.put("name", name);
        productParams.put("description", description);
        productParams.put("active", true);
        
        try {
            product.update(productParams);
            return Mono.just(product);
        } catch (StripeException e) {
            log.error("Error creating mock product", e);
            return Mono.error(e);
        }
    }
    
    /**
     * Create a mock price with dummy data
     */
    public Mono<Price> createPrice(String productId, Long unitAmount, String currency) {
        log.info("Creating mock price for product: {}, amount: {}, currency: {}", 
                productId, unitAmount, currency);
        
        Price price = new Price();
        Map<String, Object> priceParams = new HashMap<>();
        priceParams.put("id", "price_mock_" + UUID.randomUUID().toString().substring(0, 8));
        priceParams.put("product", productId);
        priceParams.put("unit_amount", unitAmount);
        priceParams.put("currency", currency);
        priceParams.put("active", true);
        
        try {
            price.update(priceParams);
            return Mono.just(price);
        } catch (StripeException e) {
            log.error("Error creating mock price", e);
            return Mono.error(e);
        }
    }
    
    /**
     * Create a mock subscription with dummy data
     */
    public Mono<Subscription> createSubscription(String customerId, String priceId) {
        log.info("Creating mock subscription for customer: {}, price: {}", customerId, priceId);
        
        Subscription subscription = new Subscription();
        Map<String, Object> subscriptionParams = new HashMap<>();
        subscriptionParams.put("id", "sub_mock_" + UUID.randomUUID().toString().substring(0, 8));
        subscriptionParams.put("customer", customerId);
        subscriptionParams.put("status", "active");
        
        try {
            subscription.update(subscriptionParams);
            return Mono.just(subscription);
        } catch (StripeException e) {
            log.error("Error creating mock subscription", e);
            return Mono.error(e);
        }
    }
    
    /**
     * Create a mock invoice with dummy data
     */
    public Mono<Invoice> createInvoice(String customerId) {
        log.info("Creating mock invoice for customer: {}", customerId);
        
        Invoice invoice = new Invoice();
        Map<String, Object> invoiceParams = new HashMap<>();
        invoiceParams.put("id", "in_mock_" + UUID.randomUUID().toString().substring(0, 8));
        invoiceParams.put("customer", customerId);
        invoiceParams.put("status", "paid");
        invoiceParams.put("amount_paid", 1000);
        invoiceParams.put("currency", "usd");
        
        try {
            invoice.update(invoiceParams);
            return Mono.just(invoice);
        } catch (StripeException e) {
            log.error("Error creating mock invoice", e);
            return Mono.error(e);
        }
    }
}
