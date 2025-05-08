-- Проверка всех таблиц в базе данных
SELECT table_schema, table_name
FROM information_schema.tables
WHERE table_schema = 'public'
AND table_type = 'BASE TABLE'
ORDER BY table_schema, table_name;

-- Проверка структуры таблицы users (или user, если она существует)
SELECT column_name, data_type, character_maximum_length, column_default, is_nullable
FROM information_schema.columns
WHERE table_schema = 'public'
AND table_name = 'users'
ORDER BY ordinal_position;

-- Альтернативная проверка для таблицы user (если она называется именно так)
SELECT column_name, data_type, character_maximum_length, column_default, is_nullable
FROM information_schema.columns
WHERE table_schema = 'public'
AND table_name = '"user"'
ORDER BY ordinal_position;

-- Проверка всех таблиц, связанных с пользователями (по имени)
SELECT table_schema, table_name
FROM information_schema.tables
WHERE table_schema = 'public'
AND (table_name LIKE '%user%' OR table_name LIKE '%User%')
ORDER BY table_schema, table_name;

-- Проверка всех последовательностей (sequences) в базе данных
SELECT sequence_schema, sequence_name
FROM information_schema.sequences
WHERE sequence_schema = 'public'
ORDER BY sequence_schema, sequence_name;

-- Проверка прав пользователя на создание таблиц
-- Если этот запрос выполняется без ошибок, значит у пользователя есть права на создание таблиц
CREATE TABLE IF NOT EXISTS test_permissions (id SERIAL PRIMARY KEY);
DROP TABLE IF EXISTS test_permissions;

-- Проверка всех схем в базе данных
SELECT schema_name
FROM information_schema.schemata
ORDER BY schema_name;
