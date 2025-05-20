#!/bin/bash

# Source environment variables
source root.env

# # Build event-service for M1
# echo "Building event-service for M1..."
# cd ../event-service
# ./mvnw clean package -DskipTests
# docker buildx build --platform linux/arm64 -t event-service:test .
# cd ../api-tests

# # Start test environment
# echo "Starting test environment..."
# docker-compose -f docker-compose-test.yml up -d

# # Wait for services to be ready
# echo "Waiting for services to be ready..."
# sleep 30

# Run specific test class if provided, otherwise run all tests
if [ -n "$1" ]; then
    ./mvnw test -Dtest=$1 \
    -Dspring.security.oauth2.client.registration.okta.client-id="$OKTA_API_SERVICES_ID" \
    -Dspring.security.oauth2.client.registration.okta.client-secret="$OKTA_API_SERVICES_SECRET" \
    -Dokta.oauth2.client-id="$OKTA_API_SERVICES_ID" \
    -Dokta.oauth2.client-secret="$OKTA_API_SERVICES_SECRET" \
    -Dspring.profiles.active=test
else
    ./mvnw test \
    -Dspring.security.oauth2.client.registration.okta.client-id="$OKTA_API_SERVICES_ID" \
    -Dspring.security.oauth2.client.registration.okta.client-secret="$OKTA_API_SERVICES_SECRET" \
    -Dokta.oauth2.client-id="$OKTA_API_SERVICES_ID" \
    -Dokta.oauth2.client-secret="$OKTA_API_SERVICES_SECRET" \
    -Dspring.profiles.active=test
fi

# Clean up test environment
# echo "Cleaning up test environment..."
# docker-compose -f docker-compose-test.yml down
