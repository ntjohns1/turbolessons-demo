package com.turbolessons.paymentservice.controller.product;

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
@Tag(name = "Product", description = "Product management APIs")
public class ProductEndpointConfig {

    private final ProductHandler handler;

    public ProductEndpointConfig(ProductHandler handler) {
        this.handler = handler;
    }

    @RouterOperations({
        @RouterOperation(
            path = "/api/payments/product",
            beanClass = ProductHandler.class,
            beanMethod = "listAll",
            operation = @Operation(
                summary = "List all products",
                description = "Returns a list of all products",
                tags = {"Product"},
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/product/{id}",
            beanClass = ProductHandler.class,
            beanMethod = "retrieve",
            operation = @Operation(
                summary = "Get product by ID",
                description = "Returns a product by ID",
                tags = {"Product"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Product ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation"),
                    @ApiResponse(responseCode = "404", description = "Product not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/product",
            beanClass = ProductHandler.class,
            beanMethod = "create",
            operation = @Operation(
                summary = "Create a new product",
                description = "Creates a new product",
                tags = {"Product"},
                responses = {
                    @ApiResponse(responseCode = "201", description = "Product created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/product/{id}",
            beanClass = ProductHandler.class,
            beanMethod = "update",
            operation = @Operation(
                summary = "Update a product",
                description = "Updates an existing product",
                tags = {"Product"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Product ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Product updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Product not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/product/{id}",
            beanClass = ProductHandler.class,
            beanMethod = "delete",
            operation = @Operation(
                summary = "Delete a product",
                description = "Deletes an existing product",
                tags = {"Product"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Product ID")
                },
                responses = {
                    @ApiResponse(responseCode = "204", description = "Product deleted"),
                    @ApiResponse(responseCode = "404", description = "Product not found")
                }
            )
        )
    })
    @Bean
    RouterFunction<ServerResponse> productRoutes() {

        return route((GET("/api/payments/product")),handler::listAll)
                .andRoute(GET("/api/payments/product/{id}"),handler::retrieve)
                .andRoute(POST("/api/payments/product"),handler::create)
                .andRoute(PUT("/api/payments/product/{id}"),handler::update)
                .andRoute(DELETE("/api/payments/product/{id}"),handler::delete);
    }
}
