package com.turbolessons.api_tests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonProperty;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml", properties = {"spring.config.name=application-test"})
public abstract class BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    
    protected RequestSpecification requestSpec;
    
    @Value("${spring.security.oauth2.client.provider.okta.token-uri}")
    private String tokenUri;
    
    @Value("${spring.security.oauth2.client.registration.okta.client-id}")
    private String clientId;
    
    @Value("${spring.security.oauth2.client.registration.okta.client-secret}")
    private String clientSecret;
    
    private String access_token;

    @BeforeAll
    public void init() {
        logger.info("Initializing BaseTest with tokenUri: {}", tokenUri);
        logger.info("Client ID: {}", clientId);
        logger.info("Client Secret length: {}", clientSecret != null ? clientSecret.length() : "null");
        
        // Initialize RestAssured with authentication
        authenticate();
    }

    private void authenticate() {
        // Obtain an access token from Okta
        try {
            access_token = obtainAccessToken();
            logger.info("Successfully obtained access token: {}", access_token != null ? "token received" : "null token");
            
            // Create a request specification with the access token
            this.requestSpec = new RequestSpecBuilder()
                    .addHeader("Authorization", "Bearer " + access_token)
                    .setContentType(ContentType.JSON)
                    .build();
    
            // Set this as the default specification for all REST Assured requests
            RestAssured.requestSpecification = this.requestSpec;
        } catch (Exception e) {
            logger.error("Error during authentication", e);
            throw e;
        }
    }

    // DPoP functionality removed as it's no longer required by Okta
    
    /**
     * Get the current access token. If no token exists yet, obtain one.
     * @return The OAuth2 access token
     */
    protected String getAccessToken() {
        logger.info("getAccessToken called, current token: {}", access_token != null ? "token exists" : "null");
        if (access_token == null || access_token.isEmpty()) {
            logger.info("Token is null or empty, obtaining new token");
            access_token = obtainAccessToken();
        }
        return access_token;
    }

    private String obtainAccessToken() {
        logger.info("Obtaining access token from: {}", tokenUri);
        RestTemplate restTemplate = new RestTemplate();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);
        
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("scope", "test_client");
        
        try {
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
            logger.info("Sending token request to Okta");
            ResponseEntity<OktaTokenResponse> response = restTemplate.exchange(
                tokenUri,
                HttpMethod.POST,
                request,
                OktaTokenResponse.class
            );
            
            OktaTokenResponse tokenResponse = response.getBody();
            if (tokenResponse == null) {
                logger.error("Failed to obtain access token from Okta: Empty response body");
                throw new RuntimeException("Failed to obtain access token from Okta: Empty response body");
            }
            
            String token = tokenResponse.getAccessToken();
            logger.info("Received token from Okta: {}", token != null ? "token received" : "null token");
            return token;
        } catch (HttpClientErrorException e) {
            logger.error("Error obtaining access token: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error obtaining access token", e);
            throw e;
        }
    }
    
    public static class OktaTokenResponse {
        @JsonProperty("access_token")
        private String access_token;
        
        public String getAccessToken() {
            return access_token;
        }
        
        public void setAccessToken(String access_token) {
            this.access_token = access_token;
        }
    }
}