package com.bruno.mypub.drinks.facade;

import com.bruno.mypub.common.query.DrinkQuery;
import com.bruno.mypub.drinks.mapper.DrinksServiceMappers;
import com.bruno.mypub.drinks.model.decorator.SpecialDrinkService;
import com.bruno.mypub.drinks.repository.entities.DrinkEntity;
import com.bruno.mypub.drinks.model.request.DrinksServiceRequest;
import com.bruno.mypub.drinks.model.response.DrinksServiceResponse;
import com.bruno.mypub.drinks.service.DrinksService;
import com.bruno.mypub.ingredients.model.request.IngredientsServiceRequest;
import com.bruno.mypub.ingredients.service.IngredientStrategyFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@AllArgsConstructor
public class DrinksServiceFacade {

    private final DrinksService drinksService;
    private final IngredientStrategyFacade ingredientStrategyFacade;
    private final SpecialDrinkService specialDrinkService;

    public Mono<DrinksServiceResponse> createDrink(DrinksServiceRequest request) {

        return Flux.fromIterable(request.getIngredients())
                .flatMap(ingredientStrategyFacade::getIngredientForDrink)
                .collectList()
                .flatMap(ingredientEntities -> {
                    DrinkEntity drinkEntity = DrinksServiceMappers.INSTANCE.drinkRequestToEntity(request);
                    drinkEntity.setIngredients(ingredientEntities);
                    return Mono.just(drinkEntity);
                }).flatMap(drinksService::createDrink)
                .flatMap(drinkEntity -> Mono.just(DrinksServiceMappers.INSTANCE.drinkEntityToResponse(drinkEntity)));
    }

    public Flux<DrinksServiceResponse> getDrinks(DrinkQuery drinkQuery) {
        return drinksService.getDrinks(drinkQuery)
                .map(DrinksServiceMappers.INSTANCE::drinkEntityToResponse);
    }

    public Mono<DrinksServiceResponse> updateDrink(String id, DrinksServiceRequest request) {

        return Flux.fromIterable(request.getIngredients())
                .flatMap(ingredientStrategyFacade::getIngredientForDrink)
                .collectList()
                .flatMap(ingredientEntities -> {
                    DrinkEntity drinkEntity = DrinksServiceMappers.INSTANCE.drinkRequestToEntity(request);
                    drinkEntity.setIngredients(ingredientEntities);
                    return Mono.just(drinkEntity);
                }).flatMap(drinkEntity -> drinksService.updateDrink(id, drinkEntity))
                .flatMap(drinkEntity -> Mono.just(DrinksServiceMappers.INSTANCE.drinkEntityToResponse(drinkEntity)));
    }

    public Mono<String> deleteDrink(String id) {
        return drinksService.deleteDrink(id);
    }

    public Mono<DrinksServiceResponse> getSpecialDrink(String id, List<IngredientsServiceRequest> ingredientRequests) {
        return specialDrinkService.getSpecialDrink(id, ingredientRequests);
    }

}
