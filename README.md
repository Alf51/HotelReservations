[![Build Status](https://travis-ci.com/coma123/Spring-Boot-Blog-REST-API.svg?branch=development)](https://travis-ci.com/coma123/Spring-Boot-Blog-REST-API) [![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=coma123_Spring-Boot-Blog-REST-API&metric=alert_status)](https://sonarcloud.io/dashboard?id=coma123_Spring-Boot-Blog-REST-API) [![CII Best Practices](https://bestpractices.coreinfrastructure.org/projects/3706/badge)](https://bestpractices.coreinfrastructure.org/projects/3706)

# Реализация по проекту Бронирование отелей "Землянка" (Spring Boot, PostgreSQL, Spring Data JPA, Junit 5, Maven)

Версия <b>REST-API</b> расположенна по адресу: https://github.com/Alf51/PrivateTemplate_HotelReservations/tree/rest-api

### Функционал программы:
1. Разбиение по ролям:
   * Аутентификация и авторизация пользователей по логину
   * Регистрация пользователей
   * Редактирование пользователя
   * Смена пароля пользователя
     
2. Получение информации по отелю
3. Добавление отеля   
4. Редактирование отеля
5. Добавление номера в отеле
6. Редактирование номера в отеле
7. Список всех отелей
8. Получение свободных номеров на заданные даты номеров в отеле
9. Получение занятых номеров на заданные даты номеров в отеле
10. Получить подробную информацию о клиенте
11. Список свободных номеров в заданном диапазоне дат
12. Получение информации о номере
13. Просмотреть подробную информацию об отеле
14. Список всех броней по логину постояльца
15. Получить подробную информацию о клиенте (о себе)
16. Отмена бронирования номера в отеле


### Для запуска необходимо:

1.Java 18

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
