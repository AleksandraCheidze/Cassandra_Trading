@echo off
echo Starting the application in development mode...
cd /d "%~dp0"
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev

pause
