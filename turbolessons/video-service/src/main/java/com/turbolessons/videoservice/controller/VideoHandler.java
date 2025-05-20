package com.turbolessons.videoservice.controller;

import com.turbolessons.videoservice.service.VideoStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.util.Map;

@Slf4j
@Service
public class VideoHandler {

    private final DefaultDataBufferFactory bufferFactory;
    private final VideoStorageClient service;

    public VideoHandler(DefaultDataBufferFactory bufferFactory, VideoStorageClient service) {
        this.bufferFactory = bufferFactory;
        this.service = service;
    }

    Mono<ServerResponse> handleGetVideo(ServerRequest r) {
        String videoId = r.pathVariable("videoId");
        return Mono.fromCallable(() -> service.getVideo(videoId)).subscribeOn(Schedulers.boundedElastic())
                .flatMap(videoInputStream -> {
                Flux<DataBuffer> videoDataBuffer = DataBufferUtils.readInputStream(() ->
                        videoInputStream, this.bufferFactory, 1024);
                return defaultReadResponse(videoDataBuffer);
        }).onErrorResume(this::handleError);
    }



    Mono<ServerResponse> handleGetAllVideos(ServerRequest r) {
        return service.getAllVideos()
                .collectList()
                .flatMap(blobInfo -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(blobInfo)))
                .onErrorResume(this::handleError);
    }


    public Mono<ServerResponse> handleSaveVideo(ServerRequest request) {
        return request.multipartData().flatMap(parts -> {
                    Map<String, Part> partMap = parts.toSingleValueMap();
                    FilePart filePart = (FilePart) partMap.get("file");
                    return service.saveVideo(filePart);
                })
                .then(ServerResponse.ok().build())
                .onErrorResume(this::handleError);
    }

    private static Mono<ServerResponse> defaultReadResponse(Publisher<DataBuffer> data) {
        return ServerResponse
                .ok()
                .contentType(new MediaType("video", "mp4"))
                .body(data, DataBuffer.class);
    }

    private Mono<ServerResponse> handleError(Throwable e) {
        log.error("Error in handleGetVideo", e);
        return ServerResponse.status(500).body(BodyInserters.fromValue(e.getMessage()));
    }

}



