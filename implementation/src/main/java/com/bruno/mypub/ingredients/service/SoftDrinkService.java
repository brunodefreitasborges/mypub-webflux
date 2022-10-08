package com.bruno.mypub.ingredients.service;

import com.bruno.mypub.common.enums.IngredientCategory;
import com.bruno.mypub.common.exceptions.NotFoundException;
import com.bruno.mypub.common.query.IngredientQuery;
import com.bruno.mypub.ingredients.mapper.IngredientsServiceMappers;
import com.bruno.mypub.ingredients.model.entities.IngredientEntity;
import com.bruno.mypub.ingredients.model.entities.SoftDrinkIngredientEntity;
import com.bruno.mypub.ingredients.model.request.IngredientsServiceRequest;
import com.bruno.mypub.ingredients.model.response.IngredientsServiceResponse;
import com.bruno.mypub.ingredients.repository.SoftDrinksRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@AllArgsConstructor
public class SoftDrinkService implements IngredientStrategy {

    private final SoftDrinksRepository softDrinksRepository;
    @Override
    public IngredientCategory getCategory() {
        return IngredientCategory.SOFT_DRINK;
    }

    @Override
    public Mono<IngredientsServiceResponse> generateIngredient(IngredientsServiceRequest request) {
        return softDrinksRepository.save(IngredientsServiceMappers.INSTANCE.ingredientRequestToSoftDrink(request))
                .map(IngredientsServiceMappers.INSTANCE::softDrinkIngredientEntityToResponse);
    }

    @Override
    public Mono<IngredientsServiceResponse> updateIngredient(String id, IngredientsServiceRequest request) {
        return softDrinksRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("ID not found: " + id)))
                .flatMap(softDrinkIngredientEntity -> {
                    SoftDrinkIngredientEntity softDrinkToSave = IngredientsServiceMappers.INSTANCE
                            .ingredientRequestToSoftDrink(request);
                    softDrinkToSave.setId(softDrinkIngredientEntity.getId());
                    softDrinkToSave.setStock(softDrinkToSave.getStock() + softDrinkIngredientEntity.getStock());
                    softDrinkToSave.setPrice(softDrinkToSave.getPrice() + softDrinkIngredientEntity.getPrice());
                    return softDrinksRepository.save(softDrinkToSave);
                }).map(IngredientsServiceMappers.INSTANCE::softDrinkIngredientEntityToResponse);
    }

    @Override
    public Flux<IngredientsServiceResponse> getIngredient(IngredientQuery query) {
        return softDrinksRepository.findSoftDrinks(query)
                .map(IngredientsServiceMappers.INSTANCE::softDrinkIngredientEntityToResponse);
    }

    @Override
    public Mono<String> deleteIngredients(String id) {
        return softDrinksRepository.customDelete(id);
    }

    @Override
    public Mono<IngredientsServiceResponse> getIngredientForDrink(IngredientsServiceRequest request) {
        return softDrinksRepository.findSoftDrinks(IngredientsServiceMappers.INSTANCE.ingredientRequestToQuery(request))
                .next().map(softDrinkIngredientEntity -> {
                    softDrinkIngredientEntity.setPrice(
                            softDrinkIngredientEntity.getPrice() / softDrinkIngredientEntity.getStock() * request.getQuantity());
                    softDrinkIngredientEntity.setStock(request.getQuantity());
                    return softDrinkIngredientEntity;
                }).map(IngredientsServiceMappers.INSTANCE::softDrinkIngredientEntityToResponse);
    }
}
