@echo off
echo Setting environment variables...

set DB_HOST=localhost
set DB_PORT=3306
set DB_NAME=treading
set DB_USERNAME=root
set DB_PASSWORD=root
set STRIPE_API_KEY=sk_test_dummy
set PAYPAL_CLIENT_ID=dummy_client_id
set PAYPAL_CLIENT_SECRET=dummy_client_secret
set PAYPAL_MODE=sandbox
set COINGECKO_API_KEY=dummy_coingecko_key
set GEMINI_API_KEY=dummy_gemini_key

echo Starting the application from JAR...
java -jar target/treading-0.0.1-SNAPSHOT.jar

pause
