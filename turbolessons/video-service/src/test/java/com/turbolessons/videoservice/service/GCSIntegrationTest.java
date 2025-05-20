//package com.turbolessons.videoservice.service;
//
//import com.google.cloud.storage.*;
//import com.google.auth.oauth2.GoogleCredentials;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.io.FileInputStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class GCSIntegrationTest {
//
//    private Storage storage;
//    private String bucketName;
//
//    @BeforeEach
//    void setUp() {
//        try {
//
//            String projectId = System.getenv("projectId");
//            String credentialsPath = System.getenv("credentialsPath");
//            bucketName = System.getenv("bucketName");
//
//
//            storage = StorageOptions.newBuilder()
//                    .setProjectId(projectId)
//                    .setCredentials(GoogleCredentials.fromStream(new FileInputStream(credentialsPath)))
//                    .build()
//                    .getService();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    void testFileUploadToGCS() {
//        try {
//            // Prepare test file
//            Path filePath = Paths.get("/Users/noslen/Movies/Good_Boy.mp4"); // Replace with the path to your test file
//
//            // Upload file to GCS
//            String blobName = "Good_Boy2.mp4"; // Specify the desired blob name
//            BlobId blobId = BlobId.of(bucketName, blobName);
//            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
//            Blob blob = storage.create(blobInfo, Files.readAllBytes(filePath));
//
//            // Assert file upload success
//            assertNotNull(blob);
//            assertEquals(blobName, blob.getName());
//            assertTrue(blob.exists());
//        } catch (Exception e) {
//            e.printStackTrace();
//            fail("File upload to GCS failed: " + e.getMessage());
//        }
//    }
//}
