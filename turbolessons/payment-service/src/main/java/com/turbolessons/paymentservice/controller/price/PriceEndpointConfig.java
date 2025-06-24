package com.turbolessons.paymentservice.controller.price;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Tag(name = "Price", description = "Price management APIs")
public class PriceEndpointConfig {

    private final PriceHandler handler;

    public PriceEndpointConfig(PriceHandler handler) {
        this.handler = handler;
    }

    @RouterOperations({
        @RouterOperation(
            path = "/api/payments/price",
            beanClass = PriceHandler.class,
            beanMethod = "listAll",
            operation = @Operation(
                summary = "List all prices",
                description = "Returns a list of all prices",
                tags = {"Price"},
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/price/{id}",
            beanClass = PriceHandler.class,
            beanMethod = "retrieve",
            operation = @Operation(
                summary = "Get price by ID",
                description = "Returns a price by ID",
                tags = {"Price"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Price ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation"),
                    @ApiResponse(responseCode = "404", description = "Price not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/price",
            beanClass = PriceHandler.class,
            beanMethod = "create",
            operation = @Operation(
                summary = "Create a new price",
                description = "Creates a new price",
                tags = {"Price"},
                responses = {
                    @ApiResponse(responseCode = "201", description = "Price created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/price/{id}",
            beanClass = PriceHandler.class,
            beanMethod = "update",
            operation = @Operation(
                summary = "Update a price",
                description = "Updates an existing price",
                tags = {"Price"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Price ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Price updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Price not found")
                }
            )
        )
    })
    @Bean
    RouterFunction<ServerResponse> priceRoutes() {

        return route((GET("/api/payments/price")),handler::listAll)
                .andRoute(GET("/api/payments/price/{id}"),handler::retrieve)
                .andRoute(POST("/api/payments/price"),handler::create)
                .andRoute(PUT("/api/payments/price/{id}"),handler::update);
    }
}
