# Ecommerce Ecosystem

A Microservice solution


Code change in order-service → 

```docker-compose up --build -d order-service```


Code change in domain → 

```docker-compose build --no-cache && docker-compose up -d```


Just restart without rebuild → 

```docker-compose restart order-service```
