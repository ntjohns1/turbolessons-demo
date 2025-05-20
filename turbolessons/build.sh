#!/bin/bash

echo "Performing a clean Maven build"
./mvnw clean package -DskipTests=true

echo "Building the config server"
cd config-service
docker build -t noslenj/demo-config-service:com-turbolessons-config-service .
cd ..

echo "Building the service registry"
cd service-registry
docker build -t noslenj/demo-service-registry:com-turbolessons-service-registry .
cd ..

echo "Building admin-service"
cd admin-service
docker build -t noslenj/demo-admin-service:com-turbolessons-admin-service .
cd ..

echo "Building event-service"
cd event-service
docker build -t noslenj/demo-event-service:com-turbolessons-event-service .
cd ..

echo "Building lessondb"
cd lessondb
docker build -t noslenj/demo-lessondb:com-turbolessons-lessondb .
cd ..

echo "Building message-service"
cd message-service
docker build -t noslenj/demo-message-service:com-turbolessons-message-service .
cd ..

echo "Building payment-service"
cd payment-service
docker build -t noslenj/demo-payment-service:com-turbolessons-payment-service .
cd ..

echo "Building video-service"
cd video-service
docker build -t noslenj/demo-video-service:com-turbolessons-video-service .
cd ..

echo "Building api-gateway"
cd api-gateway
docker build -t noslenj/demo-api-gateway:com-turbolessons-api-gateway .

