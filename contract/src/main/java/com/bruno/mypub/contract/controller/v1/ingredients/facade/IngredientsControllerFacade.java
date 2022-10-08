package com.bruno.mypub.contract.controller.v1.ingredients.facade;

import com.bruno.mypub.common.enums.IngredientCategory;
import com.bruno.mypub.common.query.IngredientQuery;
import com.bruno.mypub.contract.controller.v1.ingredients.mapper.IngredientsControllerMapper;
import com.bruno.mypub.contract.controller.v1.ingredients.model.request.IngredientsControllerRequest;
import com.bruno.mypub.contract.controller.v1.ingredients.model.response.IngredientsControllerResponse;
import com.bruno.mypub.ingredients.facade.IngredientsServiceFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class IngredientsControllerFacade {

    private final IngredientsServiceFacade ingredientsServiceFacade;

    public Mono<IngredientsControllerResponse> createNewIngredient(IngredientsControllerRequest ingredientRequest) {
        return ingredientsServiceFacade.createNewIngredient(
                IngredientsControllerMapper.INSTANCE.ingredientControllerToService(
                        ingredientRequest
                )).flatMap(ingredientServiceResponse ->
                Mono.just(IngredientsControllerMapper.INSTANCE.ingredientServiceToController(ingredientServiceResponse)));
    }

    public Mono<IngredientsControllerResponse> updateIngredient(String id, IngredientsControllerRequest request) {
        return ingredientsServiceFacade.updateIngredient(id,
                        IngredientsControllerMapper.INSTANCE.ingredientControllerToService(request))
                .flatMap(ingredientServiceResponse ->
                        Mono.just(IngredientsControllerMapper.INSTANCE.ingredientServiceToController(ingredientServiceResponse)));
    }

    public Flux<IngredientsControllerResponse> getIngredients(IngredientQuery query) {
        return ingredientsServiceFacade.getIngredients(query)
                .flatMap(ingredientServiceResponse ->
                        Mono.just(IngredientsControllerMapper.INSTANCE.ingredientServiceToController(ingredientServiceResponse)));
    }

    public Mono<String> deleteIngredients(String id, IngredientCategory category) {
        return ingredientsServiceFacade.deleteIngredients(id, category);
    }
}
