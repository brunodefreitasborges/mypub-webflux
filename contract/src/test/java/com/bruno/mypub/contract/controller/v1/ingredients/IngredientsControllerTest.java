package com.bruno.mypub.contract.controller.v1.ingredients;

import com.bruno.mypub.common.enums.IngredientCategory;
import com.bruno.mypub.common.exceptions.GlobalExceptionHandler;
import com.bruno.mypub.common.exceptions.NotFoundException;
import com.bruno.mypub.common.query.IngredientQuery;
import com.bruno.mypub.contract.controller.v1.ingredients.facade.IngredientsControllerFacade;
import com.bruno.mypub.contract.controller.v1.ingredients.model.request.IngredientsControllerRequest;
import com.bruno.mypub.contract.controller.v1.ingredients.model.response.IngredientsControllerResponse;
import com.bruno.mypub.drinks.model.decorator.SpecialDrinkService;
import com.bruno.mypub.drinks.repository.DrinksRepository;
import com.bruno.mypub.ingredients.facade.IngredientsServiceFacade;
import com.bruno.mypub.ingredients.model.entities.DestiladoIngredientEntity;
import com.bruno.mypub.ingredients.model.entities.FruitIngredientEntity;
import com.bruno.mypub.ingredients.model.entities.SoftDrinkIngredientEntity;
import com.bruno.mypub.ingredients.repository.DestiladosRepository;
import com.bruno.mypub.ingredients.repository.FruitsRepository;
import com.bruno.mypub.ingredients.repository.SoftDrinksRepository;
import com.bruno.mypub.ingredients.service.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.test.StepVerifier;

@WebFluxTest(IngredientsController.class)
@ActiveProfiles("test")
@AutoConfigureDataMongo
@EnableReactiveMongoRepositories(basePackageClasses = {
        DrinksRepository.class,
        DestiladosRepository.class,
        SoftDrinksRepository.class,
        FruitsRepository.class
})
@ContextConfiguration(classes = {
        IngredientsServiceFacade.class,
        IngredientsControllerFacade.class,
        IngredientsController.class,
        DestiladosRepository.class,
        SoftDrinksRepository.class,
        FruitsRepository.class,
        IngredientStrategyFacade.class,
        IngredientStrategy.class,
        DestiladoService.class,
        FrutaService.class,
        SoftDrinkService.class,
        SpecialDrinkService.class,
        GlobalExceptionHandler.class})
class IngredientsControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    DestiladosRepository destiladosRepository;
    @Autowired
    FruitsRepository fruitsRepository;
    @Autowired
    SoftDrinksRepository softDrinksRepository;

    @BeforeEach
    void setUp() {
        destiladosRepository.save(new DestiladoIngredientEntity(
                "1",
                IngredientCategory.DESTILADO,
                "Vodka",
                10.0,
                980,
                40.0
                )).block();

        softDrinksRepository.save(new SoftDrinkIngredientEntity(
                "1",
                IngredientCategory.SOFT_DRINK,
                "Coca Cola",
                5.0,
                1000
                )).block();

        fruitsRepository.save(new FruitIngredientEntity(
                "1",
                IngredientCategory.FRUIT,
                "Limão",
                2.0,
                500
                )).block();
    }

    @AfterEach
    void tearDown() {
        destiladosRepository.deleteAll().block();
        fruitsRepository.deleteAll().block();
        softDrinksRepository.deleteAll().block();
    }

    final IngredientsControllerRequest ingredientsControllerRequest = IngredientsControllerRequest.builder()
            .price(10.0)
            .quantity(50)
            .build();

    final IngredientQuery ingredientQuery = IngredientQuery.builder()
            .category(IngredientCategory.DESTILADO)
            .build();

    @Test
    void createNewDestiladoIngredient_ShouldReturnMonoIngredientControllerResponse_WhenSuccessful() {

        ingredientsControllerRequest.setCategory(IngredientCategory.DESTILADO);
        ingredientsControllerRequest.setName("Rum");

        webTestClient.post()
                .uri("/v1/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientsControllerRequest))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(IngredientsControllerResponse.class)
                .toString().contains("{\"name\":\"Rum\",\"category\":\"DESTILADO\"}");
    }

    @Test
    void createNewSoftDrinkIngredient_ShouldReturnMonoIngredientControllerResponse_WhenSuccessful() {

        ingredientsControllerRequest.setCategory(IngredientCategory.SOFT_DRINK);
        ingredientsControllerRequest.setName("Sprite");

        webTestClient.post()
                .uri("/v1/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientsControllerRequest))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(IngredientsControllerResponse.class)
                .toString().contains("{\"name\":\"Sprite\",\"category\":\"SOFT_DRINK\"}");
    }

    @Test
    void createNewFruitIngredient_ShouldReturnMonoIngredientControllerResponse_WhenSuccessful() {

        ingredientsControllerRequest.setCategory(IngredientCategory.FRUIT);
        ingredientsControllerRequest.setName("Maracujá");

        webTestClient.post()
                .uri("/v1/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientsControllerRequest))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(IngredientsControllerResponse.class)
                .toString().contains("{\"name\":\"Maracujá\",\"category\":\"FRUIT\"}");
    }

    @Test
    void createNewIngredient_ShouldThrowValidationException_WhenRequestIsInvalid() {

        ingredientsControllerRequest.setName("");

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientsControllerRequest))
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody().json("{" +
                        "\"errorName\":\"Validation Exception\"," +
                        "\"errorMessages\":[\"Ingredient Name cannot be empty\"]}");
    }

    @Test
    void updateDestiladoIngredient_ShouldReturnMonoIngredientControllerResponse_WhenSuccessful() {

        ingredientsControllerRequest.setCategory(IngredientCategory.DESTILADO);
        ingredientsControllerRequest.setName("Updated Vodka");

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients/")
                        .queryParam("id", "1")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientsControllerRequest))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(IngredientsControllerResponse.class)
                .toString().contains("{\"name\":\"Updated Vodka\",\"category\":\"DESTILADO\"}");
    }

    @Test
    void updateSoftDrinkIngredient_ShouldReturnMonoIngredientControllerResponse_WhenSuccessful() {

        ingredientsControllerRequest.setCategory(IngredientCategory.SOFT_DRINK);
        ingredientsControllerRequest.setName("Updated Coca Cola");

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients/")
                        .queryParam("id", "1")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientsControllerRequest))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(IngredientsControllerResponse.class)
                .toString().contains("{\"name\":\"Updated Coca Cola\",\"category\":\"SOFT_DRINK\"}");
    }

    @Test
    void updateFruitIngredient_ShouldReturnMonoIngredientControllerResponse_WhenSuccessful() {

        ingredientsControllerRequest.setCategory(IngredientCategory.FRUIT);
        ingredientsControllerRequest.setName("Updated Limão");

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients/")
                        .queryParam("id", "1")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientsControllerRequest))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(IngredientsControllerResponse.class)
                .toString().contains("{\"name\":\"Updated Limão\",\"category\":\"FRUIT\"}");
    }

    @Test
    void getIngredients_ShouldReturnAllSoftDrinks_WhenEmptyQueryIsPassed() {

        ingredientQuery.setCategory(IngredientCategory.SOFT_DRINK);

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody().json("[{" +
                        "\"id\":\"1\"," +
                        "\"name\":\"Coca Cola\"," +
                        "\"category\":\"SOFT_DRINK\"" +
                        "}]");
    }
    @Test
    void getIngredients_ShouldReturnFluxIngredientControllerResponse_WhenValidSoftDrinkIdIsPassed() {

        ingredientQuery.setCategory(IngredientCategory.SOFT_DRINK);
        ingredientQuery.setId("1");

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody().json("[{" +
                        "\"id\":\"1\"," +
                        "\"name\":\"Coca Cola\"," +
                        "\"category\":\"SOFT_DRINK\"" +
                        "}]");
    }

    @Test
    void getIngredients_ShouldReturnFluxIngredientControllerResponse_WhenValidSoftDrinkNameIsPassed() {

        ingredientQuery.setCategory(IngredientCategory.SOFT_DRINK);
        ingredientQuery.setName("Coca");

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody().json("[{" +
                        "\"id\":\"1\"," +
                        "\"name\":\"Coca Cola\"," +
                        "\"category\":\"SOFT_DRINK\"" +
                        "}]");
    }

    @Test
    void getIngredients_ShouldReturnFluxIngredientControllerResponse_WhenValidSoftDrinkPriceRangeIsPassed() {

        ingredientQuery.setCategory(IngredientCategory.SOFT_DRINK);
        ingredientQuery.setPriceFloor(2.00);
        ingredientQuery.setPriceCeil(10.00);

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody().json("[{" +
                        "\"id\":\"1\"," +
                        "\"name\":\"Coca Cola\"," +
                        "\"category\":\"SOFT_DRINK\"" +
                        "}]");
    }

    @Test
    void getIngredients_ShouldReturnFluxIngredientControllerResponse_WhenValidSoftDrinkStockThresholdIsPassed() {

        ingredientQuery.setCategory(IngredientCategory.SOFT_DRINK);
        ingredientQuery.setStock(1000);

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody().json("[{" +
                        "\"id\":\"1\"," +
                        "\"name\":\"Coca Cola\"," +
                        "\"category\":\"SOFT_DRINK\"" +
                        "}]");
    }

    @Test
    void getIngredients_ShouldThrowNotFoundException_WhenInvalidValidSoftDrinkIdIsPassed() {

        ingredientQuery.setCategory(IngredientCategory.SOFT_DRINK);
        ingredientQuery.setId("invalid");

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody().json("{" +
                        "\"errorName\":\"Not Found Exception\"," +
                        "\"errorMessages\":[\"ID not found: invalid\"]}");
    }

    @Test
    void getIngredients_ShouldThrowNotFoundException_WhenInvalidSoftDrinkNameIsPassed() {

        ingredientQuery.setCategory(IngredientCategory.SOFT_DRINK);
        ingredientQuery.setName("Sprite");

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody().json("{" +
                        "\"errorName\":\"Not Found Exception\"," +
                        "\"errorMessages\":[\"Soft Drink not found: Sprite\"]}");
    }

    @Test
    void getIngredients_ShouldReturnAllDestilados_WhenEmptyQueryIsPassed() {

        ingredientQuery.setCategory(IngredientCategory.DESTILADO);

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody().json("[{" +
                        "\"id\":\"1\"," +
                        "\"name\":\"Vodka\"," +
                        "\"category\":\"DESTILADO\"" +
                        "}]");
    }
    @Test
    void getIngredients_ShouldReturnFluxIngredientControllerResponse_WhenValidDestiladoIdIsPassed() {

        ingredientQuery.setId("1");

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody().json("[{" +
                        "\"id\":\"1\"," +
                        "\"name\":\"Vodka\"," +
                        "\"category\":\"DESTILADO\"" +
                        "}]");
    }

    @Test
    void getIngredients_ShouldReturnFluxIngredientControllerResponse_WhenValidDestiladoNameIsPassed() {

        ingredientQuery.setName("Vodka");

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody().json("[{" +
                        "\"id\":\"1\"," +
                        "\"name\":\"Vodka\"," +
                        "\"category\":\"DESTILADO\"" +
                        "}]");
    }

    @Test
    void getIngredients_ShouldReturnFluxIngredientControllerResponse_WhenValidDestiladoPriceRangeIsPassed() {

        ingredientQuery.setPriceFloor(10.00);
        ingredientQuery.setPriceCeil(100.00);

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody().json("[{" +
                        "\"id\":\"1\"," +
                        "\"name\":\"Vodka\"," +
                        "\"category\":\"DESTILADO\"" +
                        "}]");
    }

    @Test
    void getIngredients_ShouldReturnFluxIngredientControllerResponse_WhenValidDestiladoStockThresholdIsPassed() {

        ingredientQuery.setStock(1000);

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody().json("[{" +
                        "\"id\":\"1\"," +
                        "\"name\":\"Vodka\"," +
                        "\"category\":\"DESTILADO\"" +
                        "}]");
    }

    @Test
    void getIngredients_ShouldThrowNotFoundException_WhenInvalidDestiladoIdIsPassed() {

        ingredientQuery.setId("invalid");

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody().json("{" +
                        "\"errorName\":\"Not Found Exception\"," +
                        "\"errorMessages\":[\"ID not found: invalid\"]}");
    }

    @Test
    void getIngredients_ShouldThrowNotFoundException_WhenInvalidDestiladoNameIsPassed() {

        ingredientQuery.setName("Rum");

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody().json("{" +
                        "\"errorName\":\"Not Found Exception\"," +
                        "\"errorMessages\":[\"Destilado not found: Rum\"]}");
    }

    @Test
    void getIngredients_ShouldReturnAllFruits_WhenEmptyQueryIsPassed() {

        ingredientQuery.setCategory(IngredientCategory.FRUIT);

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody().json("[{" +
                        "\"id\":\"1\"," +
                        "\"name\":\"Limão\"," +
                        "\"category\":\"FRUIT\"" +
                        "}]");
    }
    @Test
    void getIngredients_ShouldReturnFluxIngredientControllerResponse_WhenValidFruitIdIsPassed() {

        ingredientQuery.setCategory(IngredientCategory.FRUIT);
        ingredientQuery.setId("1");

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody().json("[{" +
                        "\"id\":\"1\"," +
                        "\"name\":\"Limão\"," +
                        "\"category\":\"FRUIT\"" +
                        "}]");
    }

    @Test
    void getIngredients_ShouldReturnFluxIngredientControllerResponse_WhenValidFruitNameIsPassed() {

        ingredientQuery.setCategory(IngredientCategory.FRUIT);
        ingredientQuery.setName("Limão");

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody().json("[{" +
                        "\"id\":\"1\"," +
                        "\"name\":\"Limão\"," +
                        "\"category\":\"FRUIT\"" +
                        "}]");
    }

    @Test
    void getIngredients_ShouldReturnFluxIngredientControllerResponse_WhenValidFruitPriceRangeIsPassed() {

        ingredientQuery.setCategory(IngredientCategory.FRUIT);
        ingredientQuery.setPriceFloor(1.00);
        ingredientQuery.setPriceCeil(5.00);

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody().json("[{" +
                        "\"id\":\"1\"," +
                        "\"name\":\"Limão\"," +
                        "\"category\":\"FRUIT\"" +
                        "}]");
    }

    @Test
    void getIngredients_ShouldReturnFluxIngredientControllerResponse_WhenValidFruitStockThresholdIsPassed() {

        ingredientQuery.setCategory(IngredientCategory.FRUIT);
        ingredientQuery.setStock(500);

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody().json("[{" +
                        "\"id\":\"1\"," +
                        "\"name\":\"Limão\"," +
                        "\"category\":\"FRUIT\"" +
                        "}]");
    }

    @Test
    void getIngredients_ShouldThrowNotFoundException_WhenInvalidValidFruitIdIsPassed() {

        ingredientQuery.setCategory(IngredientCategory.FRUIT);
        ingredientQuery.setId("invalid");

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody().json("{" +
                        "\"errorName\":\"Not Found Exception\"," +
                        "\"errorMessages\":[\"ID not found: invalid\"]}");
    }

    @Test
    void getIngredients_ShouldThrowNotFoundException_WhenInvalidFruitNameIsPassed() {

        ingredientQuery.setCategory(IngredientCategory.FRUIT);
        ingredientQuery.setName("Maracujá");

        webTestClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody().json("{" +
                        "\"errorName\":\"Not Found Exception\"," +
                        "\"errorMessages\":[\"Fruit not found: Maracujá\"]}");
    }

    @Test
    void deleteIngredients_ShouldDeleteDestiladoFromDatabase_WhenValidDestiladoIdIsPassed() {

        ingredientQuery.setId("1");

        webTestClient.method(HttpMethod.DELETE)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients")
                        .queryParam("id", ingredientQuery.getId())
                        .queryParam("category", ingredientQuery.getCategory())
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        StepVerifier.create(destiladosRepository.findById("1"))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void deleteIngredients_ShouldDeleteSoftDrinkFromDatabase_WhenValidIdIsPassed() {

        ingredientQuery.setCategory(IngredientCategory.SOFT_DRINK);
        ingredientQuery.setId("1");

        webTestClient.method(HttpMethod.DELETE)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients/")
                        .queryParam("id", ingredientQuery.getId())
                        .queryParam("category", ingredientQuery.getCategory())
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        StepVerifier.create(softDrinksRepository.findById("1"))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void deleteIngredients_ShouldDeleteFruitFromDatabase_WhenValidIdIsPassed() {

        ingredientQuery.setCategory(IngredientCategory.FRUIT);
        ingredientQuery.setId("1");

        webTestClient.method(HttpMethod.DELETE)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients/")
                        .queryParam("id", ingredientQuery.getId())
                        .queryParam("category", ingredientQuery.getCategory())
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        StepVerifier.create(fruitsRepository.findById("1"))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void deleteIngredients_ShouldDeleteAllDestiladoDatabase_WhenIdPassedIsZero() {

        ingredientQuery.setId("0");

        webTestClient.method(HttpMethod.DELETE)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients/")
                        .queryParam("id", ingredientQuery.getId())
                        .queryParam("category", ingredientQuery.getCategory())
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        StepVerifier.create(destiladosRepository.findAll())
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void deleteIngredients_ShouldDeleteAllSoftDrinkDatabase_WhenIdPassedIsZero() {

        ingredientQuery.setCategory(IngredientCategory.SOFT_DRINK);
        ingredientQuery.setId("0");

        webTestClient.method(HttpMethod.DELETE)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients/")
                        .queryParam("id", ingredientQuery.getId())
                        .queryParam("category", ingredientQuery.getCategory())
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        StepVerifier.create(softDrinksRepository.findAll())
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void deleteIngredients_ShouldDeleteAllFruitDatabase_WhenIdPassedIsZero() {

        ingredientQuery.setCategory(IngredientCategory.FRUIT);
        ingredientQuery.setId("0");

        webTestClient.method(HttpMethod.DELETE)
                .uri(uriBuilder -> uriBuilder.path("/v1/ingredients/")
                        .queryParam("id", ingredientQuery.getId())
                        .queryParam("category", ingredientQuery.getCategory())
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ingredientQuery))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

            StepVerifier.create(fruitsRepository.findAll())
                .expectNextCount(0)
                .verifyComplete();
    }
}