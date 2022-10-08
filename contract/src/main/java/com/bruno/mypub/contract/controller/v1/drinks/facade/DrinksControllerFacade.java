package com.bruno.mypub.contract.controller.v1.drinks.facade;

import com.bruno.mypub.common.query.DrinkQuery;
import com.bruno.mypub.contract.controller.v1.drinks.mapper.DrinksControllerMapper;
import com.bruno.mypub.contract.controller.v1.drinks.model.request.DrinksControllerRequest;
import com.bruno.mypub.contract.controller.v1.drinks.model.response.DrinksControllerResponse;
import com.bruno.mypub.contract.controller.v1.ingredients.mapper.IngredientsControllerMapper;
import com.bruno.mypub.contract.controller.v1.ingredients.model.request.IngredientsControllerRequest;
import com.bruno.mypub.drinks.facade.DrinksServiceFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@AllArgsConstructor
public class DrinksControllerFacade {

    private final DrinksServiceFacade drinksServiceFacade;
    public Mono<DrinksControllerResponse> createDrink(DrinksControllerRequest request) {
        return drinksServiceFacade.createDrink(DrinksControllerMapper.INSTANCE.drinksControllerToService(request))
                .flatMap(response -> Mono.just(DrinksControllerMapper.INSTANCE.drinksServiceToController(response)));
    }

    public Flux<DrinksControllerResponse> getDrinks(DrinkQuery drinkQuery) {
        return drinksServiceFacade.getDrinks(drinkQuery)
                .map(DrinksControllerMapper.INSTANCE::drinksServiceToController);
    }

    public Mono<DrinksControllerResponse> updateDrink(String id, DrinksControllerRequest request) {
        return drinksServiceFacade.updateDrink(id, DrinksControllerMapper.INSTANCE.drinksControllerToService(request))
                .flatMap(response -> Mono.just(DrinksControllerMapper.INSTANCE.drinksServiceToController(response)));
    }

    public Mono<String> deleteDrink(String id) {
        return drinksServiceFacade.deleteDrink(id);
    }

    public Mono<DrinksControllerResponse> getSpecialDrink(String id, List<IngredientsControllerRequest> ingredientRequests) {
        return drinksServiceFacade.getSpecialDrink(id, ingredientRequests.stream()
                .map(IngredientsControllerMapper.INSTANCE::ingredientControllerToService).toList())
                .flatMap(response -> Mono.just(DrinksControllerMapper.INSTANCE.drinksServiceToController(response)));
    }
}
