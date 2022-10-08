package com.bruno.mypub.ingredients.facade;

import com.bruno.mypub.common.enums.IngredientCategory;
import com.bruno.mypub.common.query.IngredientQuery;
import com.bruno.mypub.ingredients.mapper.IngredientsServiceMappers;
import com.bruno.mypub.ingredients.model.request.IngredientsServiceRequest;
import com.bruno.mypub.ingredients.model.response.IngredientsServiceResponse;
import com.bruno.mypub.ingredients.service.IngredientStrategyFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class IngredientsServiceFacade {

    private final IngredientStrategyFacade ingredientStrategyFacade;

    public Mono<IngredientsServiceResponse> createNewIngredient(IngredientsServiceRequest ingredientRequest) {

        return ingredientStrategyFacade.createNewIngredient(ingredientRequest);
    }

    public Mono<IngredientsServiceResponse> updateIngredient(String id, IngredientsServiceRequest request) {
        return ingredientStrategyFacade.updateIngredient(id, request);
    }

    public Flux<IngredientsServiceResponse> getIngredients(IngredientQuery query) {
        return ingredientStrategyFacade.getIngredient(query);
    }

    public Mono<String> deleteIngredients(String id, IngredientCategory category) {
        return ingredientStrategyFacade.deleteIngredients(id, category);
    }
}
