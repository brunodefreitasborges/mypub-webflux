package com.bruno.mypub.ingredients.repository;

import com.bruno.mypub.common.query.IngredientQuery;
import com.bruno.mypub.ingredients.model.entities.SoftDrinkIngredientEntity;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SoftDrinksCustomRepository {

    Flux<SoftDrinkIngredientEntity> findSoftDrinks(IngredientQuery query);

    Mono<String> customDelete(String id);
}
