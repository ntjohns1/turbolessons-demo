package com.turbolessons.videoservice.service;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;
import com.turbolessons.videoservice.model.SimpleBlobInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

@Slf4j
public class VideoStorageClient {

    private final Storage storage;
    private final String bucketName;
    Bucket bucket;


        public VideoStorageClient(Storage storage, String bucketName) throws IOException {
            this.storage = storage;
            this.bucketName = bucketName;
        }

    public InputStream getVideo(String videoId) {
        Blob blob = storage.get(BlobId.of(bucketName, videoId));
        return Channels.newInputStream(blob.reader());
    }

    public Flux<SimpleBlobInfo> getAllVideos() {
        bucket = storage.get(bucketName);
        Page<Blob> blobs = bucket.list();
        return Flux.fromIterable(blobs.iterateAll())
                .map(blob -> new SimpleBlobInfo(blob.getName(), blob.getBlobId().toString()));
    }

    public Mono<Void> saveVideo(FilePart filePart) {
        String blobName = filePart.filename();
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, blobName)).build();
        WritableByteChannel channel = storage.writer(blobInfo);
        Flux<DataBuffer> content = filePart.content();
        return content.collectList().flatMap(dataBufferList -> Mono.create(sink -> {
            try {
                for (DataBuffer dataBuffer : dataBufferList) {
                    ByteBuffer byteBuffer = dataBuffer.asByteBuffer();
                    while (byteBuffer.hasRemaining()) {
                        channel.write(byteBuffer);
                    }
                }
                channel.close();
                sink.success();
            } catch (IOException e) {
                sink.error(new RuntimeException("Failed to write to the Google Cloud Storage", e));
            }
        }));
    }

}
