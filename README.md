# GIF RECEIVER
## Техническое задание
Создать сервис, который обращается к сервису курсов валют, и отдает gif в ответ:       
если курс по отношению к USD за сегодня стал выше вчерашнего, то отдаем рандомную [отсюда](https://giphy.com/search/rich)   
если ниже - с тегом broke. Брать gif c endpoint [отсюда](https://giphy.com/search/broke)  

**Ссылки**  
[REST API курсов валют](https://docs.openexchangerates.org/)   
[REST API гифок](https://developers.giphy.com/docs/api#quick-start-guide)

**Must Have**   
Сервис на Spring Boot 2 + Java / Kotlin   
Запросы приходят на HTTP endpoint (должен быть написан в соответствии с rest conventions), туда передается код валюты
по отношению с которой сравнивается USD
 - Для взаимодействия с внешними сервисами используется Feign   
 - Все параметры (валюта по отношению к которой смотрится курс, адреса внешних сервисов и т.д.) вынесены в настройки   
 - На сервис написаны тесты (для мока внешних сервисов можно использовать @mockbean или WireMock)   
 - Для сборки должен использоваться Gradle   
 - Результатом выполнения должен быть репо на GitHub с инструкцией по запуску   

**Nice to Have**   
 - Сборка и запуск Docker контейнера с этим сервисом»
***
## Stack
- Open JDK 17
- Spring Boot
- Spring Cloud(OpenFeign)
- Lombok
- JUnit 5
***
## Запуск
- Склонировать репозиторий, выполнив команду:   
  `git clone https://github.com/vitaly-lab/gif-currency-app`
- Перейдя в корневую папку проекта собрать проект:    
  `gradlew build`
- Собрать докер-образ с произвольным именем, в нашем случае gif-currency-app:    
  `docker image build -t gif-currency-app .`
- Запустить контейнер с нашим образом:   
  `docker run -p 8090:8090 docker.io/library/gif-currency-app`
***
## Endpoints
- `/api/getgif/*`  
  Возвращает гифку в зависимости от курса валют   
  **Parameters**   
  test: string (GBP)   
  **_Пример_**   
  `http://localhost:8090/api/getgif/GBP`
------
- `/api/getcodes`  
  Возвращает список валют которые можно применять    
  **_Пример_**   
  `http://localhost:8090/api/getcodes`
***
## Примечание
- API, представляющее гифки бесплатно только для базовой валюты USD
- Список доступных валют можно посмотреть [здесь](https://openexchangerates.org/api/currencies.json)
