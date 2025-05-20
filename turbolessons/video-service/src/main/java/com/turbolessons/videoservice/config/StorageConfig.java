package com.turbolessons.videoservice.config;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.turbolessons.videoservice.service.VideoStorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class StorageConfig {

    private final String bucketName;
    private final Storage storage;

    public StorageConfig(@Value("${google.credentials.path}") String credentialsPath,
                         @Value("${google.project.id}") String projectId,
                         @Value("${google.bucket.name}") String bucketName) throws IOException {
        this.bucketName = bucketName;

        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath));
        this.storage = StorageOptions
                .newBuilder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build()
                .getService();
    }

    @Bean
    public Storage storage() {
        return storage;
    }

    @Bean
    public VideoStorageClient videoStorageClient() throws IOException {
        return new VideoStorageClient(storage, bucketName);
    }
}
