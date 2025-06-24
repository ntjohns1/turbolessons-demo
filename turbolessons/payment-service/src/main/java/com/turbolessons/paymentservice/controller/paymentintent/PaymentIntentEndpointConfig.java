package com.turbolessons.paymentservice.controller.paymentintent;

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
@Tag(name = "Payment Intent", description = "Payment intent management APIs")
public class PaymentIntentEndpointConfig {

    private final PaymentIntentHandler handler;

    public PaymentIntentEndpointConfig(PaymentIntentHandler handler) {
        this.handler = handler;
    }

    @RouterOperations({
        @RouterOperation(
            path = "/api/payments/paymentintent",
            beanClass = PaymentIntentHandler.class,
            beanMethod = "listAll",
            operation = @Operation(
                summary = "List all payment intents",
                description = "Returns a list of all payment intents",
                tags = {"Payment Intent"},
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/paymentintent/{id}",
            beanClass = PaymentIntentHandler.class,
            beanMethod = "retrieve",
            operation = @Operation(
                summary = "Get payment intent by ID",
                description = "Returns a payment intent by ID",
                tags = {"Payment Intent"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Payment Intent ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation"),
                    @ApiResponse(responseCode = "404", description = "Payment intent not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/paymentintent/customer/{id}",
            beanClass = PaymentIntentHandler.class,
            beanMethod = "searchByCustomer",
            operation = @Operation(
                summary = "Find payment intents by customer",
                description = "Returns payment intents for a specific customer",
                tags = {"Payment Intent"},
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
            path = "/api/payments/paymentintent",
            beanClass = PaymentIntentHandler.class,
            beanMethod = "create",
            operation = @Operation(
                summary = "Create a new payment intent",
                description = "Creates a new payment intent",
                tags = {"Payment Intent"},
                responses = {
                    @ApiResponse(responseCode = "201", description = "Payment intent created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/paymentintent/{id}",
            beanClass = PaymentIntentHandler.class,
            beanMethod = "update",
            operation = @Operation(
                summary = "Update a payment intent",
                description = "Updates an existing payment intent",
                tags = {"Payment Intent"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Payment Intent ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Payment intent updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Payment intent not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/paymentintent/capture/{id}",
            beanClass = PaymentIntentHandler.class,
            beanMethod = "capture",
            operation = @Operation(
                summary = "Capture a payment intent",
                description = "Captures a previously authorized payment intent",
                tags = {"Payment Intent"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Payment Intent ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Payment intent captured"),
                    @ApiResponse(responseCode = "400", description = "Payment intent cannot be captured"),
                    @ApiResponse(responseCode = "404", description = "Payment intent not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/paymentintent/{id}",
            beanClass = PaymentIntentHandler.class,
            beanMethod = "cancel",
            operation = @Operation(
                summary = "Cancel a payment intent",
                description = "Cancels a payment intent",
                tags = {"Payment Intent"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Payment Intent ID")
                },
                responses = {
                    @ApiResponse(responseCode = "204", description = "Payment intent canceled"),
                    @ApiResponse(responseCode = "400", description = "Payment intent cannot be canceled"),
                    @ApiResponse(responseCode = "404", description = "Payment intent not found")
                }
            )
        )
    })
    @Bean
    RouterFunction<ServerResponse> paymentIntentRoutes() {

      return  route((GET("/api/payments/paymentintent")), handler::listAll)
                .andRoute(GET("/api/payments/paymentintent/{id}"), handler::retrieve)
                .andRoute(GET("/api/payments/paymentintent/customer/{id}"), handler::searchByCustomer)
                .andRoute(POST("/api/payments/paymentintent"), handler::create)
                .andRoute(PUT("/api/payments/paymentintent/{id}"), handler::update)
                .andRoute(PUT("/api/payments/paymentintent/capture/{id}"), handler::capture)
                .andRoute(DELETE("/api/payments/paymentintent/{id}"), handler::cancel);
    }
}
