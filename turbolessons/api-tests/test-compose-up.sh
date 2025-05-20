#!/bin/bash

# Start test environment
echo "Starting test environment..."
docker-compose -f docker-compose-test.yml up -d
