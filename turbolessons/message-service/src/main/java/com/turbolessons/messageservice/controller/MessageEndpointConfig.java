package com.turbolessons.messageservice.controller;

import com.turbolessons.messageservice.config.CaseInsensitiveRequestPredicate;
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
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Tag(name = "Messages", description = "APIs for messaging and notifications")
class MessageEndpointConfig {

    private final MessageHandler handler;

    MessageEndpointConfig(MessageHandler handler) {
        this.handler = handler;
    }


    @RouterOperations({
        @RouterOperation(
            path = "/api/messages",
            beanClass = MessageHandler.class,
            beanMethod = "all",
            operation = @Operation(
                summary = "Get all messages",
                description = "Returns a list of all messages",
                tags = {"Messages"},
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation")
                }
            )
        ),
        @RouterOperation(
            path = "/api/messages/{id}",
            beanClass = MessageHandler.class,
            beanMethod = "getById",
            operation = @Operation(
                summary = "Get message by ID",
                description = "Returns a message by ID",
                tags = {"Messages"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Message ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation"),
                    @ApiResponse(responseCode = "404", description = "Message not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/messages/sender/{sender}",
            beanClass = MessageHandler.class,
            beanMethod = "getBySender",
            operation = @Operation(
                summary = "Get messages by sender",
                description = "Returns messages sent by a specific sender",
                tags = {"Messages"},
                parameters = {
                    @Parameter(name = "sender", in = ParameterIn.PATH, description = "Sender ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation")
                }
            )
        ),
        @RouterOperation(
            path = "/api/messages/recipient/{recipient}",
            beanClass = MessageHandler.class,
            beanMethod = "getByRecipient",
            operation = @Operation(
                summary = "Get messages by recipient",
                description = "Returns messages received by a specific recipient",
                tags = {"Messages"},
                parameters = {
                    @Parameter(name = "recipient", in = ParameterIn.PATH, description = "Recipient ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation")
                }
            )
        ),
        @RouterOperation(
            path = "/api/messages/{sender}/to/{recipient}",
            beanClass = MessageHandler.class,
            beanMethod = "getBySenderAndRecipient",
            operation = @Operation(
                summary = "Get messages between sender and recipient",
                description = "Returns messages exchanged between a specific sender and recipient",
                tags = {"Messages"},
                parameters = {
                    @Parameter(name = "sender", in = ParameterIn.PATH, description = "Sender ID"),
                    @Parameter(name = "recipient", in = ParameterIn.PATH, description = "Recipient ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation")
                }
            )
        ),
        @RouterOperation(
            path = "/api/messages",
            beanClass = MessageHandler.class,
            beanMethod = "sendAll",
            operation = @Operation(
                summary = "Send messages to multiple recipients",
                description = "Sends messages to multiple recipients",
                tags = {"Messages"},
                responses = {
                    @ApiResponse(responseCode = "201", description = "Messages sent"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
                }
            )
        ),
        @RouterOperation(
            path = "/api/messages/{id}",
            beanClass = MessageHandler.class,
            beanMethod = "send",
            operation = @Operation(
                summary = "Send a message",
                description = "Sends a message with the specified ID",
                tags = {"Messages"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Message ID")
                },
                responses = {
                    @ApiResponse(responseCode = "201", description = "Message sent"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Message not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/messages/{id}",
            beanClass = MessageHandler.class,
            beanMethod = "deleteById",
            operation = @Operation(
                summary = "Delete a message",
                description = "Deletes a message by ID",
                tags = {"Messages"},
                parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH, description = "Message ID")
                },
                responses = {
                    @ApiResponse(responseCode = "204", description = "Message deleted"),
                    @ApiResponse(responseCode = "404", description = "Message not found")
                }
            )
        )
    })
    @Bean
    RouterFunction<ServerResponse> routes() {
        return route(i(GET("/api/messages")), handler::all)
                .andRoute(i(GET("/api/messages/{id}")), handler::getById)
                .andRoute(i(GET("/api/messages/sender/{sender}")), handler::getBySender)
                .andRoute(i(GET("/api/messages/recipient/{recipient}")), handler::getByRecipient)
                .andRoute(i(GET("/api/messages/{sender}/to/{recipient}")), handler::getBySenderAndRecipient)
                .andRoute(i(POST("/api/messages")), handler::sendAll)
                .andRoute(i(POST("/api/messages/{id}")), handler::send)
                .andRoute(i(DELETE("/api/messages/{id}")), handler::deleteById);
    }


    private static RequestPredicate i(RequestPredicate target) {
        return new CaseInsensitiveRequestPredicate(target);
    }
}
