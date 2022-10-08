package com.bruno.mypub.ingredients.service;

import com.bruno.mypub.common.enums.IngredientCategory;
import com.bruno.mypub.common.query.IngredientQuery;
import com.bruno.mypub.ingredients.model.entities.IngredientEntity;
import com.bruno.mypub.ingredients.model.request.IngredientsServiceRequest;
import com.bruno.mypub.ingredients.model.response.IngredientsServiceResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class IngredientStrategyFacade implements InitializingBean {

    private final List<IngredientStrategy> ingredientStrategies;
    private Map<IngredientCategory, IngredientStrategy> strategies;

    @Override
    public void afterPropertiesSet() {
        this.strategies = new EnumMap<>(IngredientCategory.class);
        ingredientStrategies.forEach((IngredientStrategy ingredientsImpl) ->
                strategies.put(ingredientsImpl.getCategory(), ingredientsImpl));
    }

    public Mono<IngredientsServiceResponse> createNewIngredient(IngredientsServiceRequest request) {
        IngredientStrategy ingredientStrategy = strategies.get(request.getCategory());
        return ingredientStrategy.generateIngredient(request);
    }


    public Mono<IngredientsServiceResponse> updateIngredient(String id, IngredientsServiceRequest request) {
        IngredientStrategy ingredientStrategy = strategies.get(request.getCategory());
        return ingredientStrategy.updateIngredient(id, request);
    }

    public Flux<IngredientsServiceResponse> getIngredient(IngredientQuery query) {
        IngredientStrategy ingredientStrategy = strategies.get(query.getCategory());
        return ingredientStrategy.getIngredient(query);
    }

    public Mono<String> deleteIngredients(String id, IngredientCategory category) {
        IngredientStrategy ingredientStrategy = strategies.get(category);
        return ingredientStrategy.deleteIngredients(id);
    }
    public Mono<IngredientsServiceResponse> getIngredientForDrink(IngredientsServiceRequest request) {
        IngredientStrategy ingredientStrategy = strategies.get(request.getCategory());
        return ingredientStrategy.getIngredientForDrink(request);
    }
}
