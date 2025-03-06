# <img src="CassandraLogo.png" alt="Logo" width="100" /> Cassandra Trading – Crypto Trading Platform 

Cassandra Trading is a secure and efficient crypto trading platform that provides users with a powerful backend for managing digital assets, processing orders, and integrating payment solutions
## 🛠 **Tech Stack**

- **Language**: ![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=openjdk&logoColor=white)  
- **Framework**:  ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat&logo=spring-boot&logoColor=white)  ![Spring Security](https://img.shields.io/badge/Spring_Security-232F3E?style=flat&logo=spring-security&logoColor=white)  
- **Database**: ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white)  
- **Authentication & Authorization**: ![Spring Security](https://img.shields.io/badge/Spring_Security-232F3E?style=flat&logo=spring-security&logoColor=white)  ![JWT](https://img.shields.io/badge/JWT-000000?style=flat&logo=json-web-tokens&logoColor=white)  
- **Email Service**: ![Java Mail Sender](https://img.shields.io/badge/Java_Mail_Sender-26A153?style=flat&logo=java&logoColor=white)  
- **API Documentation**: ![Postman](https://img.shields.io/badge/Postman-FF6C37?style=flat&logo=postman&logoColor=white)  

---
## 🚀 **Key Features**

## **Advanced Wallet Functionality**

✅**Wallet-to-Wallet Transfer**: Securely transfer funds between wallets

✅**Withdrawal to Bank Account**: Directly withdraw funds to linked bank accounts 

✅**Add Balance to Wallet**: Easily top up wallet balances using supported payment methods  


## **Comprehensive Transaction History**

✅ **Withdrawal History**: View detailed logs of all past withdrawals  

✅ **Wallet History**: Access a complete record of all wallet transactions, including deposits, transfers, and withdrawals  

✅ **Search Coin**: Effortlessly search for any cryptocurrency and get essential information for informed trading decisions  

##  **Robust Authentication and Security**
✅**Login & Registration**: Simple yet secure user authentication process  

✅ **Two-Factor Authentication (2FA)**: Add an extra layer of protection to user accounts  

✅ **Forgot Password**: Streamlined password recovery process for enhanced user experience 

##  **Real-Time Data Integration**
✅Fetch live cryptocurrency data via trusted APIs like **CoinGecko** and **Gemini**  

✅Monitor market trends and make informed trading decisions  

## **Payment Gateways**
✅Seamless integration with leading payment providers such as **PayPal** and **Stripe**  

## **Additional Features**
✅**Portfolio Analytics**: Gain insights into portfolio to monitor investments and track performance  

## 📌 API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|---------|-------------|
| POST | `/auth/signup` | Register a new user |
| POST | `/auth/signin` | Login user |
| POST | `/auth/two-factor/enable` | Enable 2FA |
| POST | `/auth/two-factor/otp/{otp}` | Verify sign-in OTP |

### Assets
| Method | Endpoint | Description |
|--------|---------|-------------|
| GET | `/api/asset/{assetId}` | Get asset by ID |
| GET | `/api/asset/coin/{coinId}/user` | Get asset by user and coin ID |
| GET | `/api/asset` | Get all assets for user |

### Orders
| Method | Endpoint | Description |
|--------|---------|-------------|
| POST | `/api/orders/pay` | Process order payment |
| GET | `/api/orders/{orderId}` | Get order by ID |
| GET | `/api/orders` | Get all orders for user |

### Payments
| Method | Endpoint | Description |
|--------|---------|-------------|
| POST | `/api/payment/{paymentMethod}/amount/{amount}` | Handle payment via PayPal/Stripe |
| POST | `/api/payment/add-details` | Add payment details |
| GET | `/api/payment/get-details` | Get user payment details |

### Withdrawals
| Method | Endpoint | Description |
|--------|---------|-------------|
| POST | `/api/withdrawal/{amount}` | Request withdrawal |
| PATCH | `/api/admin/withdrawal/{id}/proceed/{accept}` | Proceed withdrawal (Admin only) |
| GET | `/api/withdrawal` | Get withdrawal history |
| GET | `/api/admin/withdrawal` | Get all withdrawal requests (Admin only) |

### User Profile & Watchlist
| Method | Endpoint | Description |
|--------|---------|-------------|
| GET | `/api/users/profile` | Get user profile |
| POST | `/api/watchlist/create` | Create watchlist |
| PATCH | `/api/watchlist/add/coin/{coinId}` | Add item to watchlist |
| GET | `/api/watchlist/{watchlistId}` | Get watchlist by ID |

## 🔹 Installation & Setup

1️⃣ Prerequisites

Make sure you have the following installed:

Java 17+

MySQL

Maven

2️⃣ Database Setup

Create the database in MySQL:

CREATE DATABASE cassandra_trading;

git clone https://github.com/username/Cassandra_Trading.git

cd CassandraTrading-Backend

4️⃣ Configure Environment Variables

Set up the application.properties file:

spring.datasource.url=jdbc:mysql://localhost:3306/cassandra_trading

spring.datasource.username=your_username

spring.datasource.password=your_password

jwt.secret=your_secret_key

5️⃣ Run the Application

mvn spring-boot:run

The backend will start running on http://localhost:8080
