package com.turbolessons.paymentservice.config;

import com.stripe.StripeClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test") // Only load in non-test profiles
public class StripeConfig {

    @Value("${STRIPE_SECRET_KEY}")
    private String apiKey;

    @Bean
    public StripeClient stripeClient() {
        return StripeClient.builder()
                .setApiKey(apiKey)
                .build();
    }
}
