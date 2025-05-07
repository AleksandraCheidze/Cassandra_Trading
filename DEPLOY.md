# Деплой на Render

Это руководство поможет вам развернуть приложение Kassandra Trading на платформе Render.

## Предварительные требования

1. Аккаунт на [Render](https://render.com/)
2. Репозиторий проекта на GitHub

## Шаги по деплою

### 1. Создание базы данных PostgreSQL

1. Войдите в свой аккаунт Render
2. Нажмите "New" и выберите "PostgreSQL"
3. Заполните следующие поля:
   - **Name**: kassandra-trading-db (или любое другое имя)
   - **Database**: treading
   - **User**: оставьте по умолчанию
   - **Region**: выберите ближайший к вашим пользователям регион
   - **PostgreSQL Version**: 15 (или новее)
   - **Plan**: Free
4. Нажмите "Create Database"
5. После создания базы данных, сохраните следующие данные:
   - **Internal Database URL**
   - **External Database URL**
   - **PSQL Command**
   - **Database Name**
   - **Username**
   - **Password**

### 2. Создание веб-сервиса

1. В панели управления Render нажмите "New" и выберите "Web Service"
2. Подключите свой GitHub репозиторий
3. Заполните следующие поля:
   - **Name**: kassandra-trading-api (или любое другое имя)
   - **Region**: выберите тот же регион, что и для базы данных
   - **Branch**: main (или другую ветку, которую вы хотите развернуть)
   - **Root Directory**: оставьте пустым
   - **Runtime**: Java
   - **Build Command**: `./mvnw clean package -DskipTests`
   - **Start Command**: `java -jar target/treading-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod`
   - **Plan**: Free

4. В разделе "Environment Variables" добавьте следующие переменные:
   - `DB_HOST`: хост вашей PostgreSQL базы данных (из Internal Database URL)
   - `DB_PORT`: порт базы данных (обычно 5432)
   - `DB_NAME`: имя базы данных (treading)
   - `DB_USERNAME`: имя пользователя базы данных
   - `DB_PASSWORD`: пароль базы данных
   - `STRIPE_API_KEY`: ваш API ключ Stripe
   - `PAYPAL_CLIENT_ID`: ваш Client ID PayPal
   - `PAYPAL_CLIENT_SECRET`: ваш Client Secret PayPal
   - `PAYPAL_MODE`: sandbox или live
   - `COINGECKO_API_KEY`: ваш API ключ CoinGecko
   - `GEMINI_API_KEY`: ваш API ключ Gemini

5. Нажмите "Create Web Service"

### 3. Проверка деплоя

1. После завершения деплоя, Render предоставит URL вашего приложения
2. Откройте URL в браузере и убедитесь, что приложение работает
3. Проверьте доступность Swagger UI по адресу: `https://your-app-url/swagger-ui.html`

## Устранение неполадок

Если у вас возникли проблемы с деплоем:

1. Проверьте логи в панели управления Render
2. Убедитесь, что все переменные окружения настроены правильно
3. Проверьте, что база данных доступна и работает
4. Убедитесь, что приложение успешно собирается локально

## Дополнительные настройки

### Настройка автоматического деплоя

По умолчанию Render настраивает автоматический деплой при каждом пуше в выбранную ветку. Если вы хотите изменить это поведение:

1. Перейдите в настройки вашего веб-сервиса
2. В разделе "Deploy" найдите "Auto-Deploy"
3. Выберите нужную опцию: "Yes" или "No"

### Масштабирование

Если вам потребуется масштабировать приложение:

1. Перейдите в настройки вашего веб-сервиса
2. В разделе "Scaling" вы можете изменить план и настроить автоматическое масштабирование (доступно только для платных планов)
