package com.turbolessons.emailservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private ReactorLoadBalancerExchangeFilterFunction lbFunction;

    @LoadBalanced
    @Bean
    WebClient webClient() {
        return webClientBuilder
                .filter(lbFunction)
                .build();
    }
}

