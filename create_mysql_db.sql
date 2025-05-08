-- Скрипт для создания локальной MySQL базы данных

-- Создание базы данных, если она не существует
CREATE DATABASE IF NOT EXISTS kassandra_trading;

-- Использование созданной базы данных
USE kassandra_trading;

-- Создание таблицы users
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    role VARCHAR(50),
    two_factor_enabled BOOLEAN DEFAULT FALSE,
    two_factor_type VARCHAR(50),
    two_factor_email VARCHAR(255),
    two_factor_mobile VARCHAR(50)
);

-- Создание индекса для ускорения поиска по email
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
