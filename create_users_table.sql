-- Удаление таблицы, если она существует
DROP TABLE IF EXISTS users CASCADE;

-- Создание таблицы users
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    role VARCHAR(50),
    two_factor_auth JSONB DEFAULT '{"enabled": false}'::jsonb
);

-- Создание индекса для ускорения поиска по email
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- Вставка тестового пользователя
INSERT INTO users (full_name, email, password, role) 
VALUES ('Test User', 'test@example.com', 'password123', 'ROLE_CUSTOMER');

-- Проверка, что таблица создана и данные вставлены
SELECT * FROM users;
