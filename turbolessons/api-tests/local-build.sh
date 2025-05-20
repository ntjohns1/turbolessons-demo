#!/bin/bash

echo "Performing a clean Maven build"
./mvnw clean package -DskipTests=true

 echo "Building the service registry"
 cd service-registry
 docker build -t noslenj/service-registry:test .
 cd ..

echo "Building admin-service"
cd admin-service
docker build -t noslenj/admin-service:test .
cd ..


echo "Building email-service"
cd email-service
docker build -t noslenj/email-service:test .
cd ..

echo "Building event-service"
cd event-service
docker build -t noslenj/event-service:test .
cd ..

echo "Building lessondb"
cd lessondb
docker build -t noslenj/lessondb:test .
cd ..

echo "Building message-service"
cd message-service
docker build -t noslenj/message-service:test .
cd ..

echo "Building payment-service"
cd payment-service
docker build -t noslenj/payment-service:test .
cd ..

echo "Building video-service"
cd video-service
docker build -t noslenj/video-service:test .
cd ..

echo "Building api-gateway"
cd api-gateway
docker build -t noslenj/api-gateway:test .
cd ..

echo "Building api-tests"
cd api-tests
docker build -t noslenj/api-tests:test .
cd ..

echo "All services built successfully"