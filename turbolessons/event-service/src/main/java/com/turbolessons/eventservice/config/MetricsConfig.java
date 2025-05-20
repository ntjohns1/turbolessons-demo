package com.turbolessons.eventservice.config;

import org.springframework.boot.actuate.autoconfigure.metrics.SystemMetricsAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@Configuration
@Profile("integration")
@EnableAutoConfiguration(exclude = {SystemMetricsAutoConfiguration.class})
public class MetricsConfig {
    // This class disables system metrics for the integration profile
}
