package com.bruno.mypub.contract.controller.v1.drinks;

import com.bruno.mypub.common.configurations.ProducerKafkaConfig;
import com.bruno.mypub.common.enums.IngredientCategory;
import com.bruno.mypub.common.exceptions.GlobalExceptionHandler;
import com.bruno.mypub.common.query.DrinkQuery;
import com.bruno.mypub.contract.controller.v1.drinks.facade.DrinksControllerFacade;
import com.bruno.mypub.contract.controller.v1.drinks.model.request.DrinksControllerRequest;
import com.bruno.mypub.contract.controller.v1.ingredients.model.request.IngredientsControllerRequest;
import com.bruno.mypub.drinks.facade.DrinksServiceFacade;
import com.bruno.mypub.drinks.model.decorator.SpecialDrinkService;
import com.bruno.mypub.drinks.repository.DrinksRepository;
import com.bruno.mypub.drinks.repository.entities.DrinkEntity;
import com.bruno.mypub.drinks.service.DrinksService;
import com.bruno.mypub.ingredients.model.entities.DestiladoIngredientEntity;
import com.bruno.mypub.ingredients.model.entities.FruitIngredientEntity;
import com.bruno.mypub.ingredients.model.entities.SoftDrinkIngredientEntity;
import com.bruno.mypub.ingredients.repository.DestiladosRepository;
import com.bruno.mypub.ingredients.repository.FruitsRepository;
import com.bruno.mypub.ingredients.repository.SoftDrinksRepository;
import com.bruno.mypub.ingredients.service.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;

@AutoConfigureDataMongo
@WebFluxTest(DrinksController.class)
@EnableReactiveMongoRepositories(basePackageClasses = {
        DrinksRepository.class,
        DestiladosRepository.class,
        SoftDrinksRepository.class,
        FruitsRepository.class
})
@ContextConfiguration(classes = {
        DrinksController.class,
        DrinksControllerFacade.class,
        DestiladosRepository.class,
        SoftDrinksRepository.class,
        FruitsRepository.class,
        DrinksRepository.class,
        IngredientStrategyFacade.class,
        IngredientStrategy.class,
        DestiladoService.class,
        FrutaService.class,
        SoftDrinkService.class,
        SpecialDrinkService.class,
        DrinksService.class,
        DrinksServiceFacade.class,
        GlobalExceptionHandler.class})
class DrinksControllerTest {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    DrinksRepository drinksRepository;
    @Autowired
    DestiladosRepository destiladosRepository;
    @Autowired
    FruitsRepository fruitsRepository;
    @Autowired
    SoftDrinksRepository softDrinksRepository;

    final DrinksControllerRequest drinksControllerRequest = DrinksControllerRequest.builder()
            .name("Test Drink")
            .glass("Test")
            .preparation("Test")
            .ingredients(List.of(IngredientsControllerRequest.builder()
                            .name("Vodka")
                            .category(IngredientCategory.DESTILADO)
                            .build(),
                    IngredientsControllerRequest.builder()
                            .name("Limão")
                            .category(IngredientCategory.FRUIT)
                            .build(),
                    IngredientsControllerRequest.builder()
                            .name("Coca Cola")
                            .category(IngredientCategory.SOFT_DRINK)
                            .build()))
            .build();

    final DrinkQuery drinkQuery = DrinkQuery.builder()
            .build();


    @BeforeEach
    void setUp() {

        destiladosRepository.save(new DestiladoIngredientEntity(
                "1",
                IngredientCategory.DESTILADO,
                "Vodka",
                10.00,
                980,
                40.00))
                .block();

        fruitsRepository.save(new FruitIngredientEntity(
                "1",
                IngredientCategory.FRUIT,
                "Limão",
                10.00,
                980))
                .block();

        softDrinksRepository.save(new SoftDrinkIngredientEntity(
                "1",
                IngredientCategory.SOFT_DRINK,
                "Coca Cola",
                10.00,
                980))
                .block();

        drinksRepository.save(DrinkEntity.builder()
                .id("1")
                .name("Test Drink")
                .abv(10.00)
                .glass("Test Glass")
                .preparation("Test Preparation")
                .garnish("Test Garnish")
                .cost(19.90)
                .ingredients(List.of(
                        new DestiladoIngredientEntity(
                                "1",
                                IngredientCategory.DESTILADO,
                                "Test Destilado",
                                10.00,
                                980,
                                40.00),
                        new FruitIngredientEntity(
                                "1",
                                IngredientCategory.FRUIT,
                                "Test Fruit",
                                10.00,
                                980),
                        new SoftDrinkIngredientEntity(
                                "1",
                                IngredientCategory.SOFT_DRINK,
                                "Test Soft Drink",
                                10.00,
                                980))).build()).block();
    }

    @AfterEach
    void tearDown() {
        fruitsRepository.deleteAll().block();
        softDrinksRepository.deleteAll().block();
        destiladosRepository.deleteAll().block();
        drinksRepository.deleteAll().block();
    }

    @Test
    void createDrink_ShouldReturnMonoDrinkControllerResponse_WhenSuccessful() {

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/v1/drinks")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(drinksControllerRequest))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody().json("{\"name\":\"Test Drink\"}");
    }

    @Test
    void createDrink_ShouldValidationException_WhenRequestIsInvalid() {

        drinksControllerRequest.setName(null);

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/v1/drinks")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(drinksControllerRequest))
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody().json("" +
                        "{\"errorName\":\"Validation Exception\"," +
                        "\"errorMessages\":[\"Drink Name cannot be empty\"]}");
    }

    @Test
    void getDrinks_ShouldReturnFluxOfAllDrinksWithGivenName_WhenSuccessful() {

        drinkQuery.setName("Test Drink");

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/drinks")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(drinkQuery))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody().json("[{" +
                        "\"name\":\"Test Drink\"}]");
    }

    @Test
    void getDrinks_ShouldReturnFluxOfAllDrinksWithGivenId_WhenSuccessful() {
        drinkQuery.setId("1");

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/drinks")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(drinkQuery))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody().json("[{" +
                        "\"id\":\"1\"}]");
    }

    @Test
    void getDrinks_ShouldReturnFluxOfAllDrinks_WhenSuccessful() {

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/drinks")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(drinkQuery))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody().json("[{" +
                        "\"name\":\"Test Drink\"}]");
    }

    @Test
    void getDrinks_ShouldThrowNotFoundException_WhenIdNotFound() {

        drinkQuery.setId("2");

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/drinks")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(drinkQuery))
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody().json("" +
                        "{\"errorName\":\"Not Found Exception\"," +
                        "\"errorMessages\":[\"Id not found: 2\"]}");
    }

    @Test
    void getDrinks_ShouldThrowNotFoundException_WhenNameNotFound() {

        drinkQuery.setName("Invalid Name");

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/drinks")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(drinkQuery))
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody().json("" +
                        "{\"errorName\":\"Not Found Exception\"," +
                        "\"errorMessages\":[\"Name not found: Invalid Name\"]}");
    }

    @Test
    void deleteDrink_ShouldReturnMonoString_WhenSuccessful() {

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/v1/drinks/")
                        .queryParam("id", "1")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody().toString().contains("Deleted Successfully");
    }

    @Test
    void deleteDrink_ShouldDropDatabase_WhenIdPassedIsZero() {

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/v1/drinks/")
                        .queryParam("id", "0")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody().toString().contains("Database Dropped Successfully");
    }

    @Test
    void updateDrink_ShouldReturnMonoDrinksControllerResponse_WhenSuccessful() {

        drinksControllerRequest.setName("Test Drink Updated");

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("/v1/drinks")
                        .queryParam("id", "1")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(drinksControllerRequest))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody().json("{\"name\":\"Test Drink Updated\"}");
    }

    @Test
    void getSpecialDrink_ShouldReturnMonoDrinksControllerResponse_WhenSuccessful() {

        List<IngredientsControllerRequest> requests = List.of(
                IngredientsControllerRequest.builder()
                        .name("Vodka")
                        .category(IngredientCategory.DESTILADO)
                        .quantity(1)
                        .build()
        );

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/drinks/decorator")
                        .queryParam("id", "1")
                        .build())
                .body(BodyInserters.fromValue(requests))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody().json("{\"name\":\"Test Drink com Vodka\"}");
    }
}