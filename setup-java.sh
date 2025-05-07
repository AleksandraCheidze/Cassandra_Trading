#!/bin/bash

# Устанавливаем Java 17
echo "Установка Java 17..."
apt-get update
apt-get install -y openjdk-17-jdk

# Проверяем версию Java
java -version
