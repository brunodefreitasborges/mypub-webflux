package com.bruno.mypub.ingredients.service;

import com.bruno.mypub.common.enums.IngredientCategory;
import com.bruno.mypub.common.query.IngredientQuery;
import com.bruno.mypub.ingredients.model.request.IngredientsServiceRequest;
import com.bruno.mypub.ingredients.model.response.IngredientsServiceResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IngredientStrategy {

    IngredientCategory getCategory();

    Mono<IngredientsServiceResponse> generateIngredient(IngredientsServiceRequest request);

    Mono<IngredientsServiceResponse> updateIngredient(String id, IngredientsServiceRequest request);

    Flux<IngredientsServiceResponse> getIngredient(IngredientQuery query);

    Mono<String> deleteIngredients(String id);

    Mono<IngredientsServiceResponse> getIngredientForDrink(IngredientsServiceRequest request);

}
