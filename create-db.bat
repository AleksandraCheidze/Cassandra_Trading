@echo off
echo Creating MySQL database 'treading'...
echo This script assumes MySQL is installed and accessible via command line.

mysql -u root -proot -e "CREATE DATABASE IF NOT EXISTS treading;"

if %ERRORLEVEL% EQU 0 (
    echo Database 'treading' created or already exists.
) else (
    echo Failed to create database. Please check if MySQL is running and credentials are correct.
)

pause
