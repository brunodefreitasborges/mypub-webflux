package com.bruno.mypub.ingredients.service;

import com.bruno.mypub.common.enums.IngredientCategory;
import com.bruno.mypub.common.exceptions.NotFoundException;
import com.bruno.mypub.common.query.IngredientQuery;
import com.bruno.mypub.ingredients.mapper.IngredientsServiceMappers;
import com.bruno.mypub.ingredients.model.entities.DestiladoIngredientEntity;
import com.bruno.mypub.ingredients.model.entities.IngredientEntity;
import com.bruno.mypub.ingredients.model.request.IngredientsServiceRequest;
import com.bruno.mypub.ingredients.model.response.IngredientsServiceResponse;
import com.bruno.mypub.ingredients.repository.DestiladosRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class DestiladoService implements IngredientStrategy {

    private final DestiladosRepository destiladosRepository;

    @Override
    public IngredientCategory getCategory() {
        return IngredientCategory.DESTILADO;
    }

    @Override
    public Mono<IngredientsServiceResponse> generateIngredient(IngredientsServiceRequest request) {
        return destiladosRepository.save(IngredientsServiceMappers.INSTANCE.ingredientRequestToDestilado(request))
                .map(IngredientsServiceMappers.INSTANCE::destiladoIngredientEntityToResponse);
    }

    @Override
    public Mono<IngredientsServiceResponse> updateIngredient(String id, IngredientsServiceRequest request) {
        return destiladosRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("ID not found: " + id)))
                .flatMap(destiladoIngredientEntity -> {
                    DestiladoIngredientEntity destiladoToSave = IngredientsServiceMappers.INSTANCE
                            .ingredientRequestToDestilado(request);
                    destiladoToSave.setId(destiladoIngredientEntity.getId());
                    destiladoToSave.setStock(destiladoToSave.getStock() + destiladoIngredientEntity.getStock());
                    destiladoToSave.setPrice(destiladoToSave.getPrice() + destiladoIngredientEntity.getPrice());
                    return destiladosRepository.save(destiladoToSave);
                }).map(IngredientsServiceMappers.INSTANCE::destiladoIngredientEntityToResponse);
    }

    @Override
    public Flux<IngredientsServiceResponse> getIngredient(IngredientQuery destiladoQuery) {
        return destiladosRepository.findDestilados(destiladoQuery)
                .map(IngredientsServiceMappers.INSTANCE::destiladoIngredientEntityToResponse);
    }

    @Override
    public Mono<String> deleteIngredients(String id) {
        return destiladosRepository.customDelete(id);

    }

    @Override
    public Mono<IngredientsServiceResponse> getIngredientForDrink(IngredientsServiceRequest request) {
        return destiladosRepository.findDestilados(IngredientsServiceMappers.INSTANCE.ingredientRequestToQuery(request))
                .next().map(destiladoIngredientEntity -> {
                    destiladoIngredientEntity.setPrice(
                            destiladoIngredientEntity.getPrice() / destiladoIngredientEntity.getStock() * request.getQuantity());
                    destiladoIngredientEntity.setStock(request.getQuantity());
                    return destiladoIngredientEntity;
                }).map(IngredientsServiceMappers.INSTANCE::destiladoIngredientEntityToResponse);
    }
}
