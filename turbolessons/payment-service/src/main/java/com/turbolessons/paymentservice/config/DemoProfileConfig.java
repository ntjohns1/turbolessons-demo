package com.turbolessons.paymentservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

/**
 * Configuration class for demo profile
 * This class provides configuration specific to the demo environment
 */
@Configuration
@Profile("demo")
@Slf4j
public class DemoProfileConfig {

    /**
     * Log that we're running in demo mode with mocked Stripe integration
     */
    @Bean
    public String demoModeNotification(Environment environment) {
        log.info("=======================================================");
        log.info("RUNNING IN DEMO MODE WITH MOCKED STRIPE INTEGRATION");
        log.info("All Stripe API calls will return mock data");
        log.info("No actual charges will be processed");
        log.info("=======================================================");
        return "demo-mode-active";
    }
}
