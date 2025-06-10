package com.turbolessons.paymentservice;

import com.stripe.Stripe;
import com.stripe.StripeClient;
import com.turbolessons.paymentservice.config.MockStripeConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for MockStripeConfig
 */
public class MockStripeConfigTest {

    @Test
    public void testMockStripeClientCreation() {
        // Create the mock config directly
        MockStripeConfig config = new MockStripeConfig();
        
        // Get the mock client
        StripeClient client = config.mockStripeClient();
        
        // Verify client is created
        assertNotNull(client);
        
        // Initialize the API key
        String apiKey = config.initializeStripeApiKey();
        
        // Verify API key format
        assertNotNull(apiKey);
        assertTrue(apiKey.startsWith("sk_test_mock_"), 
                "API key should start with sk_test_mock_ but was: " + apiKey);
        
        // Verify the static Stripe.apiKey is set
        assertNotNull(Stripe.apiKey);
        assertTrue(Stripe.apiKey.startsWith("sk_test_mock_"), 
                "Stripe.apiKey should start with sk_test_mock_ but was: " + Stripe.apiKey);
    }
}
