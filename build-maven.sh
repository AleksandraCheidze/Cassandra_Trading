#!/bin/bash

# Используем Maven напрямую
echo "Сборка проекта с помощью Maven..."
mvn clean package -DskipTests
