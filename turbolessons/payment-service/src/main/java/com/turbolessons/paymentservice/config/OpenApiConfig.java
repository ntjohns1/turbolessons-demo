package com.turbolessons.paymentservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class for OpenAPI documentation
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configures the OpenAPI documentation for the Payment Service
     * @return OpenAPI configuration
     */
    @Bean
    public OpenAPI paymentServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Turbolessons Payment Service API")
                        .description("API for managing payments and subscriptions in the Turbolessons platform")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Turbolessons Support")
                                .url("https://turbolessons.com")
                                .email("support@turbolessons.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server()
                                .url("/")
                                .description("Default Server URL"),
                        new Server()
                                .url("http://localhost:5010")
                                .description("Local development"),
                        new Server()
                                .url("https://nelsonjohns.com")
                                .description("Production environment")
                ));
    }
}
