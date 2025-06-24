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

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Tag(name = "Subscription Item", description = "Subscription item management APIs")
public class SubscriptionItemEndpointConfig {
    
    private static final String BASE_PATH = "/api/payments/subscription_item";
    
    @RouterOperations({
        @RouterOperation(
            path = "/api/payments/subscription_item",
            beanClass = SubscriptionItemHandler.class,
            beanMethod = "listAll",
            operation = @Operation(
                summary = "List all subscription items",
                description = "Returns a list of all subscription items",
                tags = {"Subscription Item"},
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/subscription_item/{id}",
            beanClass = SubscriptionItemHandler.class,
            beanMethod = "retrieve",
            operation = @Operation(
                summary = "Get subscription item by ID",
                description = "Returns a subscription item by ID",
                tags = {"Subscription Item"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Subscription Item ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation"),
                    @ApiResponse(responseCode = "404", description = "Subscription item not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/subscription_item",
            beanClass = SubscriptionItemHandler.class,
            beanMethod = "create",
            operation = @Operation(
                summary = "Create a new subscription item",
                description = "Creates a new subscription item",
                tags = {"Subscription Item"},
                responses = {
                    @ApiResponse(responseCode = "201", description = "Subscription item created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/subscription_item/{id}",
            beanClass = SubscriptionItemHandler.class,
            beanMethod = "update",
            operation = @Operation(
                summary = "Update a subscription item",
                description = "Updates an existing subscription item",
                tags = {"Subscription Item"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Subscription Item ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Subscription item updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Subscription item not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/subscription_item/{id}",
            beanClass = SubscriptionItemHandler.class,
            beanMethod = "delete",
            operation = @Operation(
                summary = "Delete a subscription item",
                description = "Deletes an existing subscription item",
                tags = {"Subscription Item"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Subscription Item ID")
                },
                responses = {
                    @ApiResponse(responseCode = "204", description = "Subscription item deleted"),
                    @ApiResponse(responseCode = "404", description = "Subscription item not found")
                }
            )
        )
    })
    @Bean
    public RouterFunction<ServerResponse> subscriptionItemRoutes(SubscriptionItemHandler handler) {
        return route()
                .GET(BASE_PATH, accept(APPLICATION_JSON), handler::listAll)
                .GET(BASE_PATH + "/{id}", accept(APPLICATION_JSON), handler::retrieve)
                .POST(BASE_PATH, accept(APPLICATION_JSON), handler::create)
                .PUT(BASE_PATH + "/{id}", accept(APPLICATION_JSON), handler::update)
                .DELETE(BASE_PATH + "/{id}", accept(APPLICATION_JSON), handler::delete)
                .build();
    }
}
