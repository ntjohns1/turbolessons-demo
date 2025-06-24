package com.turbolessons.paymentservice.controller.invoice;

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

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
@Tag(name = "Invoice", description = "Invoice management APIs")
public class InvoiceEndpointConfig {

    private final InvoiceHandler handler;

    public InvoiceEndpointConfig(InvoiceHandler handler) {
        this.handler = handler;
    }

    @RouterOperations({
        @RouterOperation(
            path = "/api/payments/invoice",
            beanClass = InvoiceHandler.class,
            beanMethod = "listAll",
            operation = @Operation(
                summary = "List all invoices",
                description = "Returns a list of all invoices",
                tags = {"Invoice"},
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/invoice/{id}/customer",
            beanClass = InvoiceHandler.class,
            beanMethod = "listAllByCustomer",
            operation = @Operation(
                summary = "List invoices by customer",
                description = "Returns a list of invoices for a specific customer",
                tags = {"Invoice"},
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
            path = "/api/payments/invoice/{id}/subscription",
            beanClass = InvoiceHandler.class,
            beanMethod = "listAllBySubscription",
            operation = @Operation(
                summary = "List invoices by subscription",
                description = "Returns a list of invoices for a specific subscription",
                tags = {"Invoice"},
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
            path = "/api/payments/invoice/{id}",
            beanClass = InvoiceHandler.class,
            beanMethod = "retrieve",
            operation = @Operation(
                summary = "Get invoice by ID",
                description = "Returns an invoice by ID",
                tags = {"Invoice"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Invoice ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation"),
                    @ApiResponse(responseCode = "404", description = "Invoice not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/invoice/{id}/upcoming",
            beanClass = InvoiceHandler.class,
            beanMethod = "retrieveUpcoming",
            operation = @Operation(
                summary = "Get upcoming invoice",
                description = "Returns an upcoming invoice for a subscription",
                tags = {"Invoice"},
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
            path = "/api/payments/invoice",
            beanClass = InvoiceHandler.class,
            beanMethod = "create",
            operation = @Operation(
                summary = "Create a new invoice",
                description = "Creates a new invoice",
                tags = {"Invoice"},
                responses = {
                    @ApiResponse(responseCode = "201", description = "Invoice created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/invoice/{id}",
            beanClass = InvoiceHandler.class,
            beanMethod = "update",
            operation = @Operation(
                summary = "Update an invoice",
                description = "Updates an existing invoice",
                tags = {"Invoice"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Invoice ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Invoice updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Invoice not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/invoice/{id}",
            beanClass = InvoiceHandler.class,
            beanMethod = "deleteDraft",
            operation = @Operation(
                summary = "Delete a draft invoice",
                description = "Deletes a draft invoice",
                tags = {"Invoice"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Invoice ID")
                },
                responses = {
                    @ApiResponse(responseCode = "204", description = "Invoice deleted"),
                    @ApiResponse(responseCode = "400", description = "Invoice is not a draft"),
                    @ApiResponse(responseCode = "404", description = "Invoice not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/invoice/{id}/finalize",
            beanClass = InvoiceHandler.class,
            beanMethod = "finalize",
            operation = @Operation(
                summary = "Finalize an invoice",
                description = "Finalizes a draft invoice",
                tags = {"Invoice"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Invoice ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Invoice finalized"),
                    @ApiResponse(responseCode = "400", description = "Invoice is not a draft"),
                    @ApiResponse(responseCode = "404", description = "Invoice not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/invoice/{id}/pay",
            beanClass = InvoiceHandler.class,
            beanMethod = "payInvoice",
            operation = @Operation(
                summary = "Pay an invoice",
                description = "Pays an open invoice",
                tags = {"Invoice"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Invoice ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Invoice paid"),
                    @ApiResponse(responseCode = "400", description = "Invoice is not open"),
                    @ApiResponse(responseCode = "404", description = "Invoice not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/invoice/{id}/void",
            beanClass = InvoiceHandler.class,
            beanMethod = "voidInvoice",
            operation = @Operation(
                summary = "Void an invoice",
                description = "Voids an open invoice",
                tags = {"Invoice"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Invoice ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Invoice voided"),
                    @ApiResponse(responseCode = "400", description = "Invoice is not open"),
                    @ApiResponse(responseCode = "404", description = "Invoice not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/invoice/{id}/mark_uncollectible",
            beanClass = InvoiceHandler.class,
            beanMethod = "markUncollectible",
            operation = @Operation(
                summary = "Mark invoice as uncollectible",
                description = "Marks an open invoice as uncollectible",
                tags = {"Invoice"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Invoice ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Invoice marked as uncollectible"),
                    @ApiResponse(responseCode = "400", description = "Invoice is not open"),
                    @ApiResponse(responseCode = "404", description = "Invoice not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/invoice/{id}/line_items",
            beanClass = InvoiceHandler.class,
            beanMethod = "retrieveLineItems",
            operation = @Operation(
                summary = "Get invoice line items",
                description = "Returns line items for an invoice",
                tags = {"Invoice"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Invoice ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation"),
                    @ApiResponse(responseCode = "404", description = "Invoice not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/payments/invoice/{id}/upcoming/line_items",
            beanClass = InvoiceHandler.class,
            beanMethod = "retrieveUpcomingLineItems",
            operation = @Operation(
                summary = "Get upcoming invoice line items",
                description = "Returns line items for an upcoming invoice",
                tags = {"Invoice"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Subscription ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation"),
                    @ApiResponse(responseCode = "404", description = "Subscription not found")
                }
            )
        )
    })
    @Bean
    RouterFunction<ServerResponse> invoiceRoutes() {
        return route((GET("/api/payments/invoice")), handler::listAll)
                .andRoute(GET("/api/payments/invoice/{id}/customer"), handler::listAllByCustomer)
                .andRoute(GET("/api/payments/invoice/{id}/subscription"), handler::listAllBySubscription)
                .andRoute(GET("/api/payments/invoice/{id}"), handler::retrieve)
                .andRoute(GET("/api/payments/invoice/{id}/upcoming"), handler::retrieveUpcoming)
                .andRoute(POST("/api/payments/invoice"), handler::create)
                .andRoute(PUT("/api/payments/invoice/{id}"), handler::update)
                .andRoute(DELETE("/api/payments/invoice/{id}"), handler::deleteDraft)
                .andRoute(POST("/api/payments/invoice/{id}/finalize"), handler::finalize)
                .andRoute(POST("/api/payments/invoice/{id}/pay"), handler::payInvoice)
                .andRoute(POST("/api/payments/invoice/{id}/void"), handler::voidInvoice)
                .andRoute(POST("/api/payments/invoice/{id}/mark_uncollectible"), handler::markUncollectible)
                .andRoute(POST("/api/payments/invoice/{id}/line_items"), handler::retrieveLineItems)
                .andRoute(POST("/api/payments/invoice/{id}/upcoming/line_items"), handler::retrieveUpcomingLineItems);
    }
}
