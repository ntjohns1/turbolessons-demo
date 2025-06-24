package com.turbolessons.paymentservice.controller.setupintent;

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
@Tag(name = "Setup Intent", description = "Setup intent management APIs")
public class SetupIntentEndpointConfig {

    private final SetupIntentHandler handler;

    public SetupIntentEndpointConfig(SetupIntentHandler handler) {
        this.handler = handler;
    }

    @RouterOperations({
        @RouterOperation(
            path = "/api/payments/setupintent",
            beanClass = SetupIntentHandler.class,
            beanMethod = "listAll",
            operation = @Operation(
                summary = "List all setup intents",
                description = "Returns a list of all setup intents",
                tags = {"Setup Intent"},
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/setupintent/{id}",
            beanClass = SetupIntentHandler.class,
            beanMethod = "retrieve",
            operation = @Operation(
                summary = "Get setup intent by ID",
                description = "Returns a setup intent by ID",
                tags = {"Setup Intent"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Setup Intent ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation"),
                    @ApiResponse(responseCode = "404", description = "Setup intent not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/setupintent",
            beanClass = SetupIntentHandler.class,
            beanMethod = "create",
            operation = @Operation(
                summary = "Create a new setup intent",
                description = "Creates a new setup intent",
                tags = {"Setup Intent"},
                responses = {
                    @ApiResponse(responseCode = "201", description = "Setup intent created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/setupintent/confirm/{id}",
            beanClass = SetupIntentHandler.class,
            beanMethod = "confirm",
            operation = @Operation(
                summary = "Confirm a setup intent",
                description = "Confirms a setup intent",
                tags = {"Setup Intent"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Setup Intent ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Setup intent confirmed"),
                    @ApiResponse(responseCode = "400", description = "Setup intent cannot be confirmed"),
                    @ApiResponse(responseCode = "404", description = "Setup intent not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/setupintent/{id}",
            beanClass = SetupIntentHandler.class,
            beanMethod = "update",
            operation = @Operation(
                summary = "Update a setup intent",
                description = "Updates an existing setup intent",
                tags = {"Setup Intent"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Setup Intent ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Setup intent updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Setup intent not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/setupintent/{id}",
            beanClass = SetupIntentHandler.class,
            beanMethod = "cancel",
            operation = @Operation(
                summary = "Cancel a setup intent",
                description = "Cancels a setup intent",
                tags = {"Setup Intent"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Setup Intent ID")
                },
                responses = {
                    @ApiResponse(responseCode = "204", description = "Setup intent canceled"),
                    @ApiResponse(responseCode = "400", description = "Setup intent cannot be canceled"),
                    @ApiResponse(responseCode = "404", description = "Setup intent not found")
                }
            )
        )
    })
    @Bean
    RouterFunction<ServerResponse> setupIntentRoutes() {

        return route((GET("/api/payments/setupintent")),handler::listAll)
                .andRoute(GET("/api/payments/setupintent/{id}"),handler::retrieve)
                .andRoute(POST("/api/payments/setupintent"),handler::create)
                .andRoute(PUT("/api/payments/setupintent/confirm/{id}"),handler::confirm)
                .andRoute(PUT("/api/payments/setupintent/{id}"),handler::update)
                .andRoute(DELETE("/api/payments/setupintent/{id}"),handler::cancel);
    }
}
