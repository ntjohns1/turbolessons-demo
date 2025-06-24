package com.turbolessons.paymentservice.controller.customer;

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
@Tag(name = "Customer", description = "Customer management APIs")
public class CustomerEndpointConfig {

    private final CustomerHandler handler;

    public CustomerEndpointConfig(CustomerHandler handler) {
        this.handler = handler;
    }

    @RouterOperations({
        @RouterOperation(
            path = "/api/payments/customer",
            beanClass = CustomerHandler.class,
            beanMethod = "listAll",
            operation = @Operation(
                summary = "List all customers",
                description = "Returns a list of all customers",
                tags = {"Customer"},
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation", 
                               content = @Content(mediaType = "application/json", 
                               schema = @Schema(implementation = Object.class)))
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/customer/{id}",
            beanClass = CustomerHandler.class,
            beanMethod = "retrieve",
            operation = @Operation(
                summary = "Get customer by ID",
                description = "Returns a customer by ID",
                tags = {"Customer"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Customer ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation"),
                    @ApiResponse(responseCode = "404", description = "Customer not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/customer/lookup/{id}",
            beanClass = CustomerHandler.class,
            beanMethod = "search",
            operation = @Operation(
                summary = "Search customer by system ID",
                description = "Returns a customer by system ID",
                tags = {"Customer"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "System ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation"),
                    @ApiResponse(responseCode = "404", description = "Customer not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/customer",
            beanClass = CustomerHandler.class,
            beanMethod = "create",
            operation = @Operation(
                summary = "Create a new customer",
                description = "Creates a new customer",
                tags = {"Customer"},
                responses = {
                    @ApiResponse(responseCode = "201", description = "Customer created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/customer/{id}",
            beanClass = CustomerHandler.class,
            beanMethod = "update",
            operation = @Operation(
                summary = "Update an existing customer",
                description = "Updates an existing customer",
                tags = {"Customer"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Customer ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Customer updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Customer not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/customer/{id}",
            beanClass = CustomerHandler.class,
            beanMethod = "delete",
            operation = @Operation(
                summary = "Delete a customer",
                description = "Deletes a customer",
                tags = {"Customer"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Customer ID")
                },
                responses = {
                    @ApiResponse(responseCode = "204", description = "Customer deleted"),
                    @ApiResponse(responseCode = "404", description = "Customer not found")
                }
            )
        )
    })
    @Bean
    RouterFunction<ServerResponse> customerRoutes() {
        return route((GET("/api/payments/customer")), handler::listAll)
                .andRoute(GET("/api/payments/customer/{id}"), handler::retrieve)
                .andRoute(GET("/api/payments/customer/lookup/{id}"), handler::search)
                .andRoute(POST("/api/payments/customer"), handler::create)
                .andRoute(PUT("/api/payments/customer/{id}"), handler::update)
                .andRoute(DELETE("/api/payments/customer/{id}"), handler::delete);
    }
}
