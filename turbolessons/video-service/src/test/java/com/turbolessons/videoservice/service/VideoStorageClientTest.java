//package com.turbolessons.videoservice.service;
//
//import com.google.cloud.ReadChannel;
//import com.google.cloud.storage.*;
//import com.google.cloud.storage.testing.RemoteStorageHelper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.test.web.reactive.server.WebTestClient;
//
//import java.io.*;
//import java.nio.ByteBuffer;
//import java.nio.channels.Channels;
//import java.nio.channels.ReadableByteChannel;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//import static org.junit.jupiter.api.Assertions.assertArrayEquals;
//import static org.mockito.Mockito.when;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class VideoStorageClientTest {
//
//    @Mock
//    private Storage storage;
//
//    @Mock
//    private Blob blob;
//
//    private VideoStorageClient client;
//    private String bucketName;
//    private String filename;
//    private byte[] videoBytes;
//
////    @BeforeEach
////    void setUp() throws IOException {
////        // initialize mocks
////        MockitoAnnotations.openMocks(this);
////
////        bucketName = "test_bucket";
////        Path videoPath = Paths.get("/Users/noslen/Movies/Good_Boy.mp4");
////        filename = videoPath.getFileName().toString();
////        videoBytes = Files.readAllBytes(videoPath);
////
////        // set up client
////        client = new VideoStorageClient(storage, bucketName);
////
////        // mock storage's get method
////        when(storage.get(BlobId.of(bucketName, filename))).thenReturn(blob);
////
////        // mock blob's reader method
////        ReadChannel reader = new ReadableByteChannel() {
////            private ByteBuffer buf = ByteBuffer.wrap(videoBytes);
////
////            @Override
////            public int read(ByteBuffer dst) throws IOException {
////                if (!buf.hasRemaining()) {
////                    return -1; // end of stream
////                }
////                int toRead = Math.min(dst.remaining(), buf.remaining());
////                for (int i = 0; i < toRead; i++) {
////                    dst.put(buf.get());
////                }
////                return toRead;
////            }
////
////            @Override
////            public boolean isOpen() {
////                return true;
////            }
////
////            @Override
////            public void close() throws IOException {}
////        };
//        when(blob.reader()).thenReturn(reader);
//    }
//
//    @Test
//    void shouldGetFileByName() throws IOException {
//        // test the `getVideo` method
//        InputStream actualInputStream = client.getVideo(filename);
//
//        // create an expected `InputStream` from `videoBytes` for comparison
//        InputStream expectedInputStream = new ByteArrayInputStream(videoBytes);
//
//        // compare the actual and expected `InputStream`
//        // convert them to `byte[]` for comparison because `InputStream` can only be read once
//        byte[] actualBytes = actualInputStream.readAllBytes();
//        byte[] expectedBytes = expectedInputStream.readAllBytes();
//
//        assertArrayEquals(expectedBytes, actualBytes);
//    }
//}
