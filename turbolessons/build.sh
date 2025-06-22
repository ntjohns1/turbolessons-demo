#!/bin/bash

echo "Performing a clean Maven build"
mvn clean package -DskipTests=true

echo "Building the config server"
cd config-service
docker build -t noslenj/config-service:demo .
cd ..

echo "Building the service registry"
cd service-registry
docker build -t noslenj/service-registry:demo .
cd ..

echo "Building admin-service"
cd admin-service
docker build -t noslenj/admin-service:demo .
cd ..

echo "Building email-service"
cd email-service
docker build -t noslenj/email-service:demo .
cd ..

echo "Building event-service"
cd event-service
docker build -t noslenj/event-service:demo .
cd ..

echo "Building lessondb"
cd lessondb
docker build -t noslenj/lessondb:demo .
cd ..

echo "Building message-service"
cd message-service
docker build -t noslenj/message-service:demo .
cd ..

echo "Building payment-service"
cd payment-service
docker build -t noslenj/payment-service:demo .
cd ..

echo "Building video-service"
cd video-service
docker build -t noslenj/video-service:demo .
cd ..

echo "Building api-gateway"
cd api-gateway
docker build -t noslenj/api-gateway:demo .

