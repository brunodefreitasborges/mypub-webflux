package com.bruno.mypub.ingredients.repository;

import com.bruno.mypub.common.query.IngredientQuery;
import com.bruno.mypub.ingredients.model.entities.FruitIngredientEntity;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FruitCustomRepository {

    Flux<FruitIngredientEntity> findFruit(IngredientQuery query);

    Mono<String> customDelete(String id);
}
