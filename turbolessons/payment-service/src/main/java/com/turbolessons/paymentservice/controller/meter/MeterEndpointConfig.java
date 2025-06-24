package com.turbolessons.paymentservice.controller.meter;

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
@Tag(name = "Meter", description = "Usage metering APIs")
public class MeterEndpointConfig {

    private final MeterHandler handler;

    public MeterEndpointConfig(MeterHandler handler) {
        this.handler = handler;
    }

    @RouterOperations({
        @RouterOperation(
            path = "/api/payments/meter",
            beanClass = MeterHandler.class,
            beanMethod = "listAll",
            operation = @Operation(
                summary = "List all meters",
                description = "Returns a list of all usage meters",
                tags = {"Meter"},
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/meter/{id}",
            beanClass = MeterHandler.class,
            beanMethod = "retrieve",
            operation = @Operation(
                summary = "Get meter by ID",
                description = "Returns a usage meter by ID",
                tags = {"Meter"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Meter ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation"),
                    @ApiResponse(responseCode = "404", description = "Meter not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/meter",
            beanClass = MeterHandler.class,
            beanMethod = "create",
            operation = @Operation(
                summary = "Create a new meter",
                description = "Creates a new usage meter",
                tags = {"Meter"},
                responses = {
                    @ApiResponse(responseCode = "201", description = "Meter created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/meter/{id}",
            beanClass = MeterHandler.class,
            beanMethod = "update",
            operation = @Operation(
                summary = "Update a meter",
                description = "Updates an existing usage meter",
                tags = {"Meter"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Meter ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Meter updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Meter not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/meter/{id}/deactivate",
            beanClass = MeterHandler.class,
            beanMethod = "deactivate",
            operation = @Operation(
                summary = "Deactivate a meter",
                description = "Deactivates an active usage meter",
                tags = {"Meter"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Meter ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Meter deactivated"),
                    @ApiResponse(responseCode = "400", description = "Meter is not active"),
                    @ApiResponse(responseCode = "404", description = "Meter not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/meter/{id}/reactivate",
            beanClass = MeterHandler.class,
            beanMethod = "reactivate",
            operation = @Operation(
                summary = "Reactivate a meter",
                description = "Reactivates an inactive usage meter",
                tags = {"Meter"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Meter ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Meter reactivated"),
                    @ApiResponse(responseCode = "400", description = "Meter is already active"),
                    @ApiResponse(responseCode = "404", description = "Meter not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/meter_event",
            beanClass = MeterHandler.class,
            beanMethod = "createEvent",
            operation = @Operation(
                summary = "Create a meter event",
                description = "Records a usage event for metering",
                tags = {"Meter"},
                responses = {
                    @ApiResponse(responseCode = "201", description = "Event created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/debug/events",
            beanClass = MeterHandler.class,
            beanMethod = "getEvents",
            operation = @Operation(
                summary = "Get all meter events",
                description = "Debug endpoint to retrieve all meter events",
                tags = {"Meter", "Debug"},
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/debug/process-lessons",
            beanClass = MeterHandler.class,
            beanMethod = "processLessons",
            operation = @Operation(
                summary = "Process lessons",
                description = "Debug endpoint to process lesson events",
                tags = {"Meter", "Debug"},
                responses = {
                    @ApiResponse(responseCode = "200", description = "Processing triggered")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/debug/ping",
            beanClass = MeterHandler.class,
            beanMethod = "ping",
            operation = @Operation(
                summary = "Ping service",
                description = "Debug endpoint to check if service is responsive",
                tags = {"Debug"},
                responses = {
                    @ApiResponse(responseCode = "200", description = "Service is responsive")
                }
            )
        )
    })
    @Bean
    RouterFunction<ServerResponse> meterRoutes() {
        return route((GET("/api/payments/meter")), handler::listAll)
                .andRoute(GET("/api/payments/meter/{id}"), handler::retrieve)
                .andRoute(POST("/api/payments/meter"), handler::create)
                .andRoute(POST("/api/payments/meter/{id}"), handler::update)
                .andRoute(POST("/api/payments/meter/{id}/deactivate"), handler::deactivate)
                .andRoute(POST("/api/payments/meter/{id}/reactivate"), handler::reactivate)
                .andRoute(POST("/api/payments/meter_event"), handler::createEvent)
                // Debug endpoints
                .andRoute(GET("/api/payments/debug/events"), handler::getEvents)
                .andRoute(GET("/api/payments/debug/process-lessons"), handler::processLessons)
                .andRoute(GET("/api/payments/debug/ping"), handler::ping);
    }
}
