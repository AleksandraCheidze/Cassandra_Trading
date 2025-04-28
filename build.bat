@echo off
echo Building the application...
call mvnw.cmd clean package -DskipTests
echo Build completed.
pause
