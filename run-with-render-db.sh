#!/bin/bash
echo "Starting application with Render PostgreSQL database..."
export SPRING_PROFILES_ACTIVE=render
./mvnw spring-boot:run
