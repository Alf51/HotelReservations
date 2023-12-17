[![Build Status](https://travis-ci.com/coma123/Spring-Boot-Blog-REST-API.svg?branch=development)](https://travis-ci.com/coma123/Spring-Boot-Blog-REST-API) [![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=coma123_Spring-Boot-Blog-REST-API&metric=alert_status)](https://sonarcloud.io/dashboard?id=coma123_Spring-Boot-Blog-REST-API) [![CII Best Practices](https://bestpractices.coreinfrastructure.org/projects/3706/badge)](https://bestpractices.coreinfrastructure.org/projects/3706)

# Реализация по проекту Бронирование отелей "Землянка" (Spring Boot, PostgreSQL, Spring Data JPA, Junit 5, Maven)

Версия <b>REST-API</b> расположенна по адресу: https://github.com/Alf51/PrivateTemplate_HotelReservations/tree/rest-api

### Программа позволяет:
1. Разбиение по ролям:
    * Аутентификация и авторизация пользователей по логину

Регистрация пользователей

Редактирование пользователя

Смена пароля пользователя

Общие требования:

получение информации по отелю (название, кол-во звёзд, адрес, отзывы, кол-во свободных номеров)

Требования к роли Администратор:

Добавление отеля

Редактирование отеля

Добавление номера в отеле

Редактирование номера в отеле

Список всех отелей

Получение свободных на заданные даты номеров в отеле

Получение занятых на заданные даты номеров в отеле

Получение броней по отелю на дату

Получить подробную информацию о постояльце

Требования к роли Постоялец/гость:

Список свободных номеров в заданном диапазоне дат

Получение информации о номере

Просмотреть подробную информацию об отеле

список всех броней по имени постояльца

получить подробную информацию о постояльце (о себе)

Отмена бронирования номера в отеле


### Для запуска необходимо:

1.Java 17

2.База данных PostgeSQL


## Инструкция по запуску

**1. Скачайте этот репозиторий к себе на компьютер**

```bash
git clone https://github.com/Alf51/PrivateTemplate_HotelReservations.git
```

**2. Создайте базу данных PosgreSQL**

Скрип для создание записей в БД расположен по пути: 
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


![segment](https://api.segment.io/v1/pixel/track?data=ewogICJ3cml0ZUtleSI6ICJwcDJuOTU4VU1NT21NR090MWJXS0JQd0tFNkcydW51OCIsCiAgInVzZXJJZCI6ICIxMjNibG9nYXBpMTIzIiwKICAiZXZlbnQiOiAiQmxvZ0FwaSB2aXNpdGVkIiwKICAicHJvcGVydGllcyI6IHsKICAgICJzdWJqZWN0IjogIkJsb2dBcGkgdmlzaXRlZCIsCiAgICAiZW1haWwiOiAiY29tcy5zcHVyc0BnbWFpbC5jb20iCiAgfQp9)
