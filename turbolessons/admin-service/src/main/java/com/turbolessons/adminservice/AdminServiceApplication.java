package com.turbolessons.adminservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//

@EnableDiscoveryClient
@SpringBootApplication
public class AdminServiceApplication {

    @Value("${okta.client.token}")
    private static String apiToken;

    public static void main(String[] args) {
		System.out.println(apiToken);
        SpringApplication.run(AdminServiceApplication.class, args);
    }

}
