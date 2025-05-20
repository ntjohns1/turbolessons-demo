package com.turbolessons.videoservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;

@Configuration
public class AppConfig {

    @Bean
    public DefaultDataBufferFactory defaultDataBufferFactory() {
        return new DefaultDataBufferFactory();
    }

}
