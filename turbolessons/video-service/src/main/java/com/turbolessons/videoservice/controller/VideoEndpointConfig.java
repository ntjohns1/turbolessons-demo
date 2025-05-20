package com.turbolessons.videoservice.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;


import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class VideoEndpointConfig {

    private final VideoHandler handler;

    public VideoEndpointConfig(VideoHandler handler) {
        this.handler = handler;
    }
    @Bean
    RouterFunction<ServerResponse> routes() {
        return route((GET("/api/video/{videoId}")), handler::handleGetVideo)
                .andRoute(GET("/api/video"), handler::handleGetAllVideos)
                .andRoute(POST("/api/video"), handler::handleSaveVideo);
    }
}
