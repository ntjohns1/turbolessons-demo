package com.turbolessons.eventservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Turbolessons Event Service API")
                        .description("API for managing lesson events in the Turbolessons platform")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Turbolessons Support")
                                .email("nelsontjohns@gmail.com")
                                .url("https://nelsonjohns.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("/").description("Default Server URL"),
                        new Server().url("http://localhost:8080").description("Local development"),
                        new Server().url("https://nelsonjohns.com").description("Production environment")
                ));
    }
}
