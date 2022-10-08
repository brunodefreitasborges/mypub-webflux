# My Pub



## Comprehensive overview

This project was developed in order to practice and better understand various concepts of Reactive Programming using the Spring Webflux framework.
It began as a simple CRUD to add different types of ingredients in order to create another CRUD of drinks.<br>

It is separated into modules concerning each layer of the application.<br>

Since it's creation, I focused into developing a clean and easily maintainable code, by making use of Design Patterns.

As more topics and technologies that were of interest to me were added, the project grew into what it is now.

## How it works

- There are 2 controllers: the Ingredients Controller and the Drinks Controller. Each one provides the simple CRUD operations.
- There is also a route in the Drinks Controller to test optional Ingredients *(Example: Gin Tonic with different types of fruits)* by making use
of the **Decorator Pattern**.
- In order to create a Drink, one or more Ingredients are needed.
- The Drinks's ABV (Alcohol by Volume) is calculated based on the Ingredients ABV.
- The Drink's Cost is calculated based on the sum of the Ingredients' costs and their quantities.
- Each distinct type of Ingredient is stored in its own database.
- Ingredients are categorized via an Enum, and to separated the logic concerning each type I implemented the **Strategy Pattern**.
- Drinks also provide a database for persistence.
- Every operation on the Drinks Controller generates an event broadcasted using Apache Kafka.
- Email Microservice consumes these events in order to forward emails containing the messages.
- The entire properties for the project are added using Spring Cloud Config.

## Technologies Used So Far

- Java(8+)
- Spring WebFlux
- Spring Cloud Config
- MongoDB
- Apache Kafka
- Docker
- Mapstruct

## To Be Implemented

- API Documentation using **Swagger**.
- Microservice, that simulates the sale of a Drink, checking the Ingredients availability before hand.
- Logic that reduces the Ingredients' stock after the sale of a Drink.



