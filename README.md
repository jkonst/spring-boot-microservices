# USD to BTC buy API
This is a Spring Boot Microservice implementation consisting of 3 
microservice APIs 
- account-service
- order-service
- btc-currency-service (wraps the requests to http://127.0.0.1:5000/btc-price)

, one Eureka discovery service and a gateway

## Python BTC currency API
### Install

```
pip install -r requirements.txt
```

### Run

```
python exchange.py
```

## Microservice End-Points
### Build and Start Eureka discovery service
```
cd registry-service
./mvnw spring-boot:run
```
### Build and Start Gateway
```
cd gateway
./mvnw spring-boot:run
```
### Build and Start Account service
```
cd account-service
./mvnw spring-boot:run
```
### Build and Start Order service
```
cd order-service
./mvnw spring-boot:run
```
### Build and Start BTC Currency service
```
cd btc-currency-service
./mvnw spring-boot:run
```
## Sample Requests
Gateway runs on port **9191**
### Create Account
```
curl --location --request POST 'http://localhost:9191/account/' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name":"John Konstas",
    "usdBalance":3000
}'
```

### Fetch Account Details
```
curl --location --request GET 'http://localhost:9191/account/1'
```

### Create Limit Order
```
curl --location --request POST 'http://localhost:9191/order/' \
--header 'Content-Type: application/json' \
--data-raw '{
    "accountId":1,
    "priceLimit":2800.56,
    "amount": "1"
}'
```

### Fetch Order Details
```
curl --location --request GET 'http://localhost:9191/order/1'
```

