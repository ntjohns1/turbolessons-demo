package com.turbolessons.paymentservice.controller.paymentmethod;

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
@Tag(name = "Payment Method", description = "Payment method management APIs")
public class PaymentMethodEndpointConfig {

    private final PaymentMethodHandler handler;

    public PaymentMethodEndpointConfig(PaymentMethodHandler handler) {
        this.handler = handler;
    }


    @RouterOperations({
        @RouterOperation(
            path = "/api/payments/paymentmethod/{id}",
            beanClass = PaymentMethodHandler.class,
            beanMethod = "retrieve",
            operation = @Operation(
                summary = "Get payment method by ID",
                description = "Returns a payment method by ID",
                tags = {"Payment Method"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Payment Method ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation"),
                    @ApiResponse(responseCode = "404", description = "Payment method not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/paymentmethod/customer/{id}",
            beanClass = PaymentMethodHandler.class,
            beanMethod = "retrieveByCustomer",
            operation = @Operation(
                summary = "Get payment methods by customer",
                description = "Returns all payment methods for a specific customer",
                tags = {"Payment Method"},
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
            path = "/api/payments/paymentmethod/attach/{id}/{customerId}",
            beanClass = PaymentMethodHandler.class,
            beanMethod = "attach",
            operation = @Operation(
                summary = "Attach payment method to customer",
                description = "Attaches a payment method to a specific customer",
                tags = {"Payment Method"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Payment Method ID"),
                    @Parameter(name = "customerId", in = ParameterIn.PATH, description = "Customer ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Payment method attached"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Payment method or customer not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/paymentmethod/detach/{id}",
            beanClass = PaymentMethodHandler.class,
            beanMethod = "detach",
            operation = @Operation(
                summary = "Detach payment method",
                description = "Detaches a payment method from its customer",
                tags = {"Payment Method"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Payment Method ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Payment method detached"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Payment method not found")
                }
            )
        )
    })
    @Bean
    RouterFunction<ServerResponse> paymentMethodRoutes() {
        return route((GET("/api/payments/paymentmethod/{id}")), handler::retrieve)
                .andRoute(GET("/api/payments/paymentmethod/customer/{id}"), handler::retrieveByCustomer)
                .andRoute(PUT("/api/payments/paymentmethod/attach/{id}/{customerId}"),handler::attach)
                .andRoute(PUT("/api/payments/paymentmethod/detach/{id}"),handler::detach);
    }
}