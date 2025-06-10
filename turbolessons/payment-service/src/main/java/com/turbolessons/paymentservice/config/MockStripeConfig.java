package com.turbolessons.paymentservice.config;

import com.stripe.Stripe;
import com.stripe.StripeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.UUID;

/**
 * Mock Stripe configuration for demo environment
 * This provides a simplified mock implementation of Stripe client for demo purposes
 */
@Configuration
@Profile("demo")
@Slf4j
public class MockStripeConfig {

    @Bean
    @Primary
    public StripeClient mockStripeClient() {
        log.info("Creating mock Stripe client for demo environment");
        // Use a dummy API key for the demo environment
        String dummyApiKey = "sk_test_mock_" + UUID.randomUUID().toString().substring(0, 8);
        return StripeClient.builder()
                .setApiKey(dummyApiKey)
                .build();
    }
    
    /**
     * This bean initializes the Stripe API with a mock key for the demo environment
     * This is needed because some code might use the static Stripe.apiKey
     */
    @Bean
    public String initializeStripeApiKey() {
        String dummyApiKey = "sk_test_mock_" + UUID.randomUUID().toString().substring(0, 8);
        Stripe.apiKey = dummyApiKey;
        log.info("Initialized Stripe API with mock key for demo environment");
        return dummyApiKey;
    }
}
