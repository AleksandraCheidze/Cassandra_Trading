#!/bin/bash

echo "Устанавливаем права на исполнение для mvnw"
chmod +x mvnw

echo "Сборка проекта с помощью Maven Wrapper..."
./mvnw clean package -DskipTests
