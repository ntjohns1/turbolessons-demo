package com.turbolessons.paymentservice.config;

import com.stripe.StripeClient;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
public class TestStripeConfig {

    @Bean
    @Primary
    public StripeClient stripeClient() {
        // Return a mock StripeClient for tests
        return Mockito.mock(StripeClient.class);
    }
}
