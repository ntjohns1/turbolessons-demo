package com.turbolessons.paymentservice.controller.invoice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class InvoiceEndpointConfig {

    private final InvoiceHandler handler;

    public InvoiceEndpointConfig(InvoiceHandler handler) {
        this.handler = handler;
    }

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
