package com.turbolessons.adminservice.config;

import com.okta.sdk.authc.credentials.TokenClientCredentials;
import com.okta.sdk.client.Clients;
import org.openapitools.client.ApiClient;
import org.openapitools.client.api.GroupApi;
import org.openapitools.client.api.UserApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OktaApiConfig {

    @Value("${okta.client.orgUrl}")
    private String orgUrl;

    @Value("${okta.client.token}")
    private String apiToken;

    @Bean
    public UserApi userApi() {
        ApiClient client = Clients.builder()
                .setOrgUrl(orgUrl)
                .setClientCredentials(new TokenClientCredentials(apiToken))
                .build();
        return new UserApi(client);
    }

    @Bean
    public GroupApi groupApi() {
        ApiClient client = Clients.builder()
                .setOrgUrl(orgUrl)
                .setClientCredentials(new TokenClientCredentials(apiToken))
                .build();
        return new GroupApi(client);
    }
}
