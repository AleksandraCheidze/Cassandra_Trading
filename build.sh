#!/bin/bash

# Установка переменной JAVA_HOME, если она не установлена
if [ -z "$JAVA_HOME" ]; then
  echo "JAVA_HOME не установлена, используем системную Java"
  export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
  echo "JAVA_HOME установлена в $JAVA_HOME"
fi

# Делаем mvnw исполняемым
chmod +x mvnw

# Используем mvn напрямую, если mvnw не работает
if ./mvnw clean package -DskipTests; then
  echo "Сборка с mvnw успешно завершена"
else
  echo "Сборка с mvnw не удалась, пробуем использовать mvn"
  mvn clean package -DskipTests
fi
