# GIF RECEIVER
## Description
Create a service that calls the exchange rate service and returns a gif in response:
if the rate against USD for today has become higher than yesterday, then we give a random tag,
if lower - with the broken tag.
***
## Stack
- Open JDK 17
- Spring Boot
- Spring Cloud(OpenFeign)
- Lombok
- JUnit 5
***
## Instruction about setting .key
- set environment variable GIPHY_API_KEY for giphy service
  https://developers.giphy.com/dashboard/?
- set environment variable CURRENCY_API_KEY for giphy service
  https://openexchangerates.org/account/app-ids
***
## Endpoints
- `/api/v1/gif/*`  
  Returns a gif depending on the exchange rate:   
  **Parameters**   
  test: string (GBP)   
  **_Example_**   
  `http://localhost:8090/api/v1/gif/GBP`
------
- `/api/v1/currency/codes`  
  Returns a list of currencies that can be used:   
  **_Example_**   
  `http://localhost:8090/api/v1/currency/codes`
***
## Note
- API that renders gifs for free for USD base currency only
