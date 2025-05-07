@echo off
echo Building JAR file...

REM Set environment variables for Maven
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

REM Build the project with Maven
call mvn clean package -DskipTests

echo.
echo Build completed. JAR file should be in the target directory.
echo.

dir /b target\*.jar

pause
