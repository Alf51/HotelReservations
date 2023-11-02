[![Build Status](https://travis-ci.com/coma123/Spring-Boot-Blog-REST-API.svg?branch=development)](https://travis-ci.com/coma123/Spring-Boot-Blog-REST-API) [![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=coma123_Spring-Boot-Blog-REST-API&metric=alert_status)](https://sonarcloud.io/dashboard?id=coma123_Spring-Boot-Blog-REST-API) [![CII Best Practices](https://bestpractices.coreinfrastructure.org/projects/3706/badge)](https://bestpractices.coreinfrastructure.org/projects/3706)

# Реализация REST Api по проекту Бронирование отелей (Spring Boot, PostgreSql, Spring Data JPA, Rest API)

### Для запуска необходимо:

1.Java 17

2.База данных PostgeSQL


## Инструкция по запуску

**1. Скачайте этот репозиторий к себе на компьютер**

```bash
git clone https://github.com/Alf51/PrivateTemplate_HotelReservations.git
```

**2. Создайте базу данных PosgreSQL**

Скрип для создание БД раположен по пути: \scripts\postgreSQL\init.sql


**3. Переименуйте application.properties.origin в application.properties**
application.properties.origin раположен по пути \src\main\resources\application.properties.origin

**4. В application.propertiesn установите праметры свой PosgreSQL БД**

**5. Запустите приложение через консоль**
Перейдите в корень проекта:
Для windows - .\mvnw.cmd package
Для unix систем - .\mvnw package

Появиться папка target и нужно перейти в неё ---> cd target
Теперь запускаем наш скомпилированный файл, на конце которого jar ----> java -jar .\PrivatePR-0.0.1-SNAPSHOT.jar

Приложение запуститься по умолчанию на хосте <http://localhost:8080>

## Изучите REST API

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
| GET    | /{id_hotel}/allRooms | Получить все комнаты по id отеля ||
| DELETE | /rooms/{id_room} | удалить комнату по id | |


## Пример

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

![segment](https://api.segment.io/v1/pixel/track?data=ewogICJ3cml0ZUtleSI6ICJwcDJuOTU4VU1NT21NR090MWJXS0JQd0tFNkcydW51OCIsCiAgInVzZXJJZCI6ICIxMjNibG9nYXBpMTIzIiwKICAiZXZlbnQiOiAiQmxvZ0FwaSB2aXNpdGVkIiwKICAicHJvcGVydGllcyI6IHsKICAgICJzdWJqZWN0IjogIkJsb2dBcGkgdmlzaXRlZCIsCiAgICAiZW1haWwiOiAiY29tcy5zcHVyc0BnbWFpbC5jb20iCiAgfQp9)
