[![Build Status](https://travis-ci.com/coma123/Spring-Boot-Blog-REST-API.svg?branch=development)](https://travis-ci.com/coma123/Spring-Boot-Blog-REST-API) [![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=coma123_Spring-Boot-Blog-REST-API&metric=alert_status)](https://sonarcloud.io/dashboard?id=coma123_Spring-Boot-Blog-REST-API) [![CII Best Practices](https://bestpractices.coreinfrastructure.org/projects/3706/badge)](https://bestpractices.coreinfrastructure.org/projects/3706)

# Реализация REST Api по проекту Бронирование отелей (Spring Boot, PostgreSQL, Spring Data JPA, Rest API)

### Для запуска необходимо:

1.Java 17

2.База данных PostgeSQL


## Инструкция по запуску

**1. Скачайте этот репозиторий к себе на компьютер**

```bash
git clone https://github.com/Alf51/PrivateTemplate_HotelReservations.git
```

**2. Создайте базу данных PosgreSQL**

Скрип для создание БД раположен по пути: 
```bash
\scripts\postgreSQL\init.sql
```

**3. Переименуйте application.properties.origin в application.properties**

application.properties.origin раположен по пути
```bash
\src\main\resources\application.properties.origin
```

**4. В application.propertiesn установите праметры своей PosgreSQL БД**

**5. Запустите приложение через консоль**

Перейдите в корень проекта и выполните команду.

Для windows: 
```bash
.\mvnw.cmd package
```

Для unix систем: 
```bash
.\mvnw package
```
Пример на windows:

Появиться папка <b>target</b> и нужно перейти в неё  ---> ``` cd target ```

Теперь запускаем наш скомпилированный файл, с расширением  <b>.jar</b>  ----> ```java -jar .\PrivatePR-0.0.1-SNAPSHOT.jar```

Приложение запуститься по умолчанию на хосте ```<http://localhost:8080>```


## Изучите REST API
Коллекция запросов для postma расположена по пути ``` .\postman\```

### Auth
| Method | Url | Description | Sample Valid Request Body |
| ------ | --- | ----------- | ------------------------- |
| POST   | /login | Sing in |[JSON](#SingIn) |
| POST   | /logout | logout | |


### Hotel
| Method | Url | Description | Sample Valid Request Body |
| ------ | --- | ----------- | ------------------------- |
| GET    | /hotel/{id_hotel} | Получить отель по id | |
| POST   | /hotel/new | Создать новый отель |[JSON](#hoteldate) |
| DELETE | /hotel/{id_hotel} | Удалить отель по id | |
| PATCH  | /hotel/{id_hotel} | Обновить отель по id |[JSON](#hotelupdate)|
| GET    | /hotel/all | Получить все отели | |

### Room
| Method | Url | Description | Sample Valid Request Body |
| ------ | --- | ----------- | ------------------------- |
| GET    | /rooms/{id_room}| Получить комнату по id | |
| POST   | /rooms/{id_hotel}/new | Создать комнату в отели по id отеля |[JSON](#roomnew) |
| PATCH  | /rooms/{id_room} | Обновить комнату по id |[JSON](#roomupdate) |
| GET    | /rooms/{id_hotel}/allRooms | Получить все комнаты по id отеля ||
| DELETE | /rooms/{id_room} | удалить комнату по id | |

### Client
| Method | Url | Description | Sample Valid Request Body |
| ------ | --- | ----------- | ------------------------- |
| GET    | /client/{id_client}| Получить клиента по id | |
| POST   | /client/new | Создать нового клиента          |[JSON](#clientnew) |
| PATCH  | /client/{id_client} | Обновить клиента по id |[JSON](#clientupdate) |
| GET    | /client/all | Получить всех клиентов ||
| DELETE | /client/{id_client} | удалить клиента по id | |

### Review
| Method | Url | Description | Sample Valid Request Body |
| ------ | --- | ----------- | ------------------------- |
| POST   | /review/new| Создать ревью по id отеля |[JSON](#reviewnew) |
| GET    | /review/{id_review} | Получить ревью по id          | |
| PATCH  | /review/{id_review} | Обновить ревью по id |[JSON](#reviewupdate) |
| GET    | /review/{id_hotel}/allHotelReviews | Получить все ревью по ID отеля ||
| DELETE | /review/{id_review} | Удалить ревью по id | |
| GET    | /review/{id_client}/allClientReviews | Получить все ревью по ID клиента ||

### Book
| Method | Url | Description | Sample Valid Request Body |
| ------ | --- | ----------- | ------------------------- |
| GET    | /books/{id_book}| Получить бронь по id | |
| POST   | /books/new | Создать бронь          | [JSON](#booknew)|
| PATCH  | /books/{id_book}| Обновить бронь по id |[JSON](#bookupdate) |
| DELETE | /books/{id_book} | Удалить бронь по id | |
| GET    | /books/{client_login}/allClientBooks | получить все брони клиента по логину клиента ||
| GET    | /books/{id_room}/allRoomBooks| получить все брони по id комнаты ||
| GET    | /books/all| получить все брони ||


## Пример

##### <a id="SingIn"> Sing in -> /login </a>
```json
{
    "login": "dorn51",
    "password": "123456"
}
```


##### <a id="hoteldate">Cоздать отель -> /hotel/new </a>
```json
{
        "name": "Antaras",
        "address": "Moscow",
        "rating": 4,
        "description": "Do you want survivor ?",
        "roomList": [
            {
                "roomNumber": 10,
                "roomSize": 10,
                "available": true
            },
            {
                "roomNumber": 11,
                "roomSize": 10,
                "available": true
            },
            {
                "roomNumber": 12,
                "roomSize": 10,
                "available": false
            }
        ]
    }
```

##### <a id="hotelupdate">Обновить отель по id -> /hotel/1 </a>
```json
{    
    "name" : "xXx",
    "address" : "Moscow",
    "rating" : 5,
    "description" : "Favorite placey"
}
```

##### <a id="roomnew">Создать комнату в отели по id отеля -> /rooms/1/new </a>
```json
{
    "roomNumber": "303",
    "roomSize": 73.82,
    "available": true
}
```

##### <a id="roomupdate">Обновить комнату по id -> /rooms/8 </a>
```json
{
    "roomNumber": "303",
    "roomSize": 73.82,
    "available": true
}
```

##### <a id="clientnew"> Создать нового клиента -> /client/new </a>
```json
{
        "name": "Penelopa",
        "login": "Crus",        
        "birthdate": "11-11-1991",
        "password" : "123456"
}
```

##### <a id="clientupdate"> Обновить клиента по id -> /client/4 </a>
```json
{
        "name": "Clepa",
        "login": "Crus",        
        "birthdate": "11-11-1991",
        "password" : "123456"
}
```

##### <a id="reviewnew"> Создать ревью по id отеля -> /review/new </a>
```json
{
    "hotelId" : 1,
    "rating": 4,
    "clientLogin": "dorn51",
    "review": "Есть баня, остальное так себе"
}
```

##### <a id="reviewupdate"> Обновить ревью по id -> /review/5 </a>
```json
{
    "hotelId" : 2,
    "rating": 5,
    "clientLogin": "utlra",
    "review": "Есть баня и это хорошо!"
}
```

##### <a id="booknew"> Создать бронь  -> /books/new </a>
```json
{
    "checkIn": "05-12-3030",
    "checkOut": "10-12-3030",
    "clientLogin": "kalibanforever",
    "roomId": 2
}
```

##### <a id="bookupdate"> Обновить бронь по id -> /books/6 </a>
```json
{
    "checkIn": "03-12-2023",
    "checkOut": "10-12-2023",
    "clientLogin": "dorn51",
    "roomId": 3
}
```

![segment](https://api.segment.io/v1/pixel/track?data=ewogICJ3cml0ZUtleSI6ICJwcDJuOTU4VU1NT21NR090MWJXS0JQd0tFNkcydW51OCIsCiAgInVzZXJJZCI6ICIxMjNibG9nYXBpMTIzIiwKICAiZXZlbnQiOiAiQmxvZ0FwaSB2aXNpdGVkIiwKICAicHJvcGVydGllcyI6IHsKICAgICJzdWJqZWN0IjogIkJsb2dBcGkgdmlzaXRlZCIsCiAgICAiZW1haWwiOiAiY29tcy5zcHVyc0BnbWFpbC5jb20iCiAgfQp9)
