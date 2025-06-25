package com.turbolessons.videoservice.controller;

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
@Tag(name = "Video", description = "APIs for video streaming and management")
public class VideoEndpointConfig {

    private final VideoHandler handler;

    public VideoEndpointConfig(VideoHandler handler) {
        this.handler = handler;
    }
    @RouterOperations({
        @RouterOperation(
            path = "/api/video/{videoId}",
            beanClass = VideoHandler.class,
            beanMethod = "handleGetVideo",
            operation = @Operation(
                summary = "Get video by ID",
                description = "Returns a video by ID",
                tags = {"Video"},
                parameters = {
                    @Parameter(name = "videoId", in = ParameterIn.PATH, description = "Video ID")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation"),
                    @ApiResponse(responseCode = "404", description = "Video not found")
                }
            )
        ),
        @RouterOperation(
            path = "/api/video",
            beanClass = VideoHandler.class,
            beanMethod = "handleGetAllVideos",
            operation = @Operation(
                summary = "Get all videos",
                description = "Returns a list of all videos",
                tags = {"Video"},
                responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation")
                }
            )
        ),
        @RouterOperation(
            path = "/api/video",
            beanClass = VideoHandler.class,
            beanMethod = "handleSaveVideo",
            operation = @Operation(
                summary = "Save a new video",
                description = "Uploads and saves a new video",
                tags = {"Video"},
                responses = {
                    @ApiResponse(responseCode = "201", description = "Video created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
                }
            )
        )
    })
    @Bean
    RouterFunction<ServerResponse> routes() {
        return route((GET("/api/video/{videoId}")), handler::handleGetVideo)
                .andRoute(GET("/api/video"), handler::handleGetAllVideos)
                .andRoute(POST("/api/video"), handler::handleSaveVideo);
    }
}
