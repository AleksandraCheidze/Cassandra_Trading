@echo off
echo Starting application with Render PostgreSQL database...

:: Устанавливаем профиль render
set SPRING_PROFILES_ACTIVE=render

:: Запускаем приложение
echo Running with profile: %SPRING_PROFILES_ACTIVE%
mvnw spring-boot:run
