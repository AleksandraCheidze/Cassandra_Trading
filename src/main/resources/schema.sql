-- Удаление таблицы, если она существует
DROP TABLE IF EXISTS users CASCADE;

-- Создание таблицы users
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
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
