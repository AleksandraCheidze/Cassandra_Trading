@echo off
echo Starting application with local MySQL database...

:: Устанавливаем профиль default для локальной разработки
set SPRING_PROFILES_ACTIVE=default

:: Запускаем приложение
echo Running with profile: %SPRING_PROFILES_ACTIVE%
mvnw spring-boot:run
