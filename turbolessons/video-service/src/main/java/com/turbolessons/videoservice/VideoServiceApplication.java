package com.turbolessons.videoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class VideoServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(VideoServiceApplication.class, args);
	}

}
