package com.turbolessons.paymentservice.controller.subscription;

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
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Tag(name = "Subscription", description = "Subscription management APIs")
public class SubscriptionEndpointConfig {
    private final SubscriptionHandler handler;

    public SubscriptionEndpointConfig(SubscriptionHandler handler) {
        this.handler = handler;
    }

    @RouterOperations({
        @RouterOperation(
            path = "/api/payments/subscription",
            beanClass = SubscriptionHandler.class,
            beanMethod = "listAll",
            operation = @Operation(
                summary = "List all subscriptions",
                description = "Returns a list of all subscriptions",
                tags = {"Subscription"},
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/subscription/{id}",
            beanClass = SubscriptionHandler.class,
            beanMethod = "retrieve",
            operation = @Operation(
                summary = "Get subscription by ID",
                description = "Returns a subscription by ID",
                tags = {"Subscription"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Subscription ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation"),
                    @ApiResponse(responseCode = "404", description = "Subscription not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/subscription",
            beanClass = SubscriptionHandler.class,
            beanMethod = "create",
            operation = @Operation(
                summary = "Create a new subscription",
                description = "Creates a new subscription",
                tags = {"Subscription"},
                responses = {
                    @ApiResponse(responseCode = "201", description = "Subscription created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/subscription/{id}",
            beanClass = SubscriptionHandler.class,
            beanMethod = "update",
            operation = @Operation(
                summary = "Update a subscription",
                description = "Updates an existing subscription",
                tags = {"Subscription"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Subscription ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Subscription updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Subscription not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/subscription/{id}",
            beanClass = SubscriptionHandler.class,
            beanMethod = "cancel",
            operation = @Operation(
                summary = "Cancel a subscription",
                description = "Cancels an existing subscription",
                tags = {"Subscription"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Subscription ID")
                },
                responses = {
                    @ApiResponse(responseCode = "204", description = "Subscription canceled"),
                    @ApiResponse(responseCode = "400", description = "Subscription cannot be canceled"),
                    @ApiResponse(responseCode = "404", description = "Subscription not found")
                }
            )
        )
    })
    @Bean
    RouterFunction<ServerResponse> subscriptionRoutes() {

        return route((GET("/api/payments/subscription")), handler::listAll)
                .andRoute(GET("/api/payments/subscription/{id}"), handler::retrieve)
                .andRoute(POST("/api/payments/subscription"), handler::create)
                .andRoute(PUT("/api/payments/subscription/{id}"), handler::update)
                .andRoute(DELETE("/api/payments/subscription/{id}"), handler::cancel);
    }
}
