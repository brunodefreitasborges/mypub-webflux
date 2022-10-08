package com.bruno.mypub.ingredients.service;

import com.bruno.mypub.common.enums.IngredientCategory;
import com.bruno.mypub.common.exceptions.NotFoundException;
import com.bruno.mypub.common.query.IngredientQuery;
import com.bruno.mypub.ingredients.mapper.IngredientsServiceMappers;
import com.bruno.mypub.ingredients.model.entities.FruitIngredientEntity;
import com.bruno.mypub.ingredients.model.entities.IngredientEntity;
import com.bruno.mypub.ingredients.model.request.IngredientsServiceRequest;
import com.bruno.mypub.ingredients.model.response.IngredientsServiceResponse;
import com.bruno.mypub.ingredients.repository.FruitsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@AllArgsConstructor
public class FrutaService implements IngredientStrategy{

    private final FruitsRepository fruitsRepository;

    @Override
    public IngredientCategory getCategory() {
        return IngredientCategory.FRUIT;
    }

    @Override
    public Mono<IngredientsServiceResponse> generateIngredient(IngredientsServiceRequest request) {
        return fruitsRepository.save(IngredientsServiceMappers.INSTANCE.ingredientRequestToFruit(request))
                .map(IngredientsServiceMappers.INSTANCE::fruitIngredientEntityToResponse);
    }

    @Override
    public Mono<IngredientsServiceResponse> updateIngredient(String id, IngredientsServiceRequest request) {
        return fruitsRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("ID not found: " + id)))
                .flatMap(fruitIngredientEntity -> {
                    FruitIngredientEntity fruitToSave = IngredientsServiceMappers.INSTANCE
                            .ingredientRequestToFruit(request);
                    fruitToSave.setId(fruitIngredientEntity.getId());
                    fruitToSave.setStock(fruitToSave.getStock() + fruitIngredientEntity.getStock());
                    fruitToSave.setPrice(fruitToSave.getPrice() + fruitIngredientEntity.getPrice());
                    return fruitsRepository.save(fruitToSave);
                }).map(IngredientsServiceMappers.INSTANCE::fruitIngredientEntityToResponse);
    }

    @Override
    public Flux<IngredientsServiceResponse> getIngredient(IngredientQuery fruitQuery) {
        return fruitsRepository.findFruit(fruitQuery)
                .map(IngredientsServiceMappers.INSTANCE::fruitIngredientEntityToResponse);
    }

    @Override
    public Mono<String> deleteIngredients(String id) {
            return fruitsRepository.customDelete(id);
    }

    @Override
    public Mono<IngredientsServiceResponse> getIngredientForDrink(IngredientsServiceRequest request) {
        return fruitsRepository.findFruit(IngredientsServiceMappers.INSTANCE.ingredientRequestToQuery(request))
                .next().map(fruitIngredientEntity -> {
                    fruitIngredientEntity.setPrice(
                            fruitIngredientEntity.getPrice() / fruitIngredientEntity.getStock() * request.getQuantity());
                    fruitIngredientEntity.setStock(request.getQuantity());
                    return fruitIngredientEntity;
                }).map(IngredientsServiceMappers.INSTANCE::fruitIngredientEntityToResponse);
    }
}
